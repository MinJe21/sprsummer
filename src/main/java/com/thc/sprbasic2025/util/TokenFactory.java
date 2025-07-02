package com.thc.sprbasic2025.util;

import com.thc.sprbasic2025.domain.RefreshToken;
import com.thc.sprbasic2025.mapper.UserMapper;
import com.thc.sprbasic2025.repository.RefreshTokenRepository;
import com.thc.sprbasic2025.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Component
public class TokenFactory {

    final RefreshTokenRepository refreshTokenRepository;

    private int refreshTokenTerm = 14 * 24 * 60;
    private int accessTokenTerm = 14 * 24 * 60;

    public String generateRefreshToken(Long id){
        return generate(id, "refreshToken");
    }
    public Long validateRefreshToken(String key){
        RefreshToken refreshToken = refreshTokenRepository.findByContent(key);
        Long userId = validate(key);
        if(userId != null && userId > 0){
            if(userId.equals(refreshToken.getUserId())){
                return userId;
            }
        }
        return null;
    }
    public String generateAccessToken(String key){
        Long id = validateRefreshToken(key);
        if(id != null && id > 0){
            return generate(id, "accessToken");
        }
        return null;
    }

    public String generate(Long id, String type){
        String returnData = null;
        String duedate = null;
        if("refreshToken".equals(type)){
            duedate = new NowDate().due(refreshTokenTerm);
        } else {
            duedate = new NowDate().due(accessTokenTerm);
        }

        String data = id + "_" + duedate;
        try{
            returnData = AES256Cipher.AES_Encode(null, data);
        } catch (Exception e){
        }
        System.out.println("returnData : " + returnData);
        if("refreshToken".equals(type)){
            if(returnData != null){
                //나는 중복로그인 안할 경우!
                List<RefreshToken> list =  refreshTokenRepository.findByUserId(id);
                for(RefreshToken each : list){
                    refreshTokenRepository.delete(each);
                }

                //리프레시 토큰 저장!
                refreshTokenRepository.save(RefreshToken.of(id, duedate, returnData));
            }
        }
        System.out.println("returnData22 : " + returnData);
        return returnData;
    }

    public Long validate(String key){
        Long data = null;
        try{
            String returnData = "";
            returnData = AES256Cipher.AES_Decode(null, key);
            String[] arrayData = returnData.split("_");
            if(arrayData.length == 2){

                String now = new NowDate().getNow();
                String duedate = arrayData[1];

                String[] tempArray = {now, duedate};
                Arrays.sort(tempArray); // 더 이른 글자순으로 정렬 됨.
                if(now.equals(tempArray[0])){
                    //아직 기한 남았을 경우!
                    data = Long.parseLong(arrayData[0]);
                } else {
                    // 기한 지난 경우!
                    data = (long) -100;
                }
            }
        } catch (Exception e){
        }
        return data;
    }
}

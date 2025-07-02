package com.thc.sprbasic2025.domain;

import com.thc.sprbasic2025.dto.DefaultDto;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Entity
public class Notice extends AuditingFields{
    String title;
    String content;

    protected Notice(){}
    private Notice(Boolean deleted, String title, String content){
        this.deleted = deleted;
        this.title = title;
        this.content = content;
    }
    public static Notice of(String title, String content){
        return new Notice(false, title, content);
    }
    public DefaultDto.CreateResDto toCreateResDto() {
        return DefaultDto.CreateResDto.builder().id(getId()).build();
    }
}

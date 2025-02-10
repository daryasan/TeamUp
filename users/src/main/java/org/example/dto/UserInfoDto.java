package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    //  id контакты, ссылка на гитхаб, опыт, описание, фото
    String token;
    String contacts;
    String github;
    String experience;
    String description;
    // TODO try and store images correctly
    String image;
}

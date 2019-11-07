package masssh.boilerplate.spring.web.dao;

import masssh.boilerplate.spring.web.model.row.OAuth2GoogleRow;
import org.apache.ibatis.annotations.*;
import java.util.Optional;

@Mapper
public interface OAuth2GoogleDao {
    @Select("SELECT * FROM oauth2_google WHERE subject = #{subject}")
    Optional<OAuth2GoogleRow> single(@Param("subject") String subject);

    @Insert("INSERT INTO oauth2_google VALUES ( #{subject}, #{idToken}, #{accessToken}, #{issuedAt}, #{expiresAt} )")
    void create(OAuth2GoogleRow oAuth2GoogleRow);

    @Update("UPDATE oauth2_google SET subject=#{subject}, idToken=#{idToken}, accessToken=#{accessToken}, issuedAt=#{issuedAt}, expiresAt=#{expiresAt} WHERE subject = #{subject}")
    void update(OAuth2GoogleRow oAuth2GoogleRow);

    @Delete("DELETE FROM oauth2_google WHERE subject = #{subject} WHERE subject = #{subject}")
    void delete(@Param("subject") String subject);
}

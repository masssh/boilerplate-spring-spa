package masssh.boilerplate.spring.spa.dao;

import masssh.boilerplate.spring.spa.model.row.OAuth2GoogleRow;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Mapper
public interface OAuth2GoogleDao {
    @Select("SELECT * FROM oauth2_google WHERE oauth2GoogleId = #{oauth2GoogleId}")
    Optional<OAuth2GoogleRow> single(@Param("oauth2GoogleId") long oauth2GoogleId);

    @Select("SELECT * FROM oauth2_google WHERE subject = #{subject}")
    Optional<OAuth2GoogleRow> singleBySubject(@Param("subject") String subject);

    @Insert("INSERT INTO oauth2_google (subject, idToken, accessToken, issuedAt, expiresAt, createdAt, updatedAt)" +
            "VALUES ( #{subject}, #{idToken}, #{accessToken}, #{issuedAt}, #{expiresAt}, #{createdAt}, #{updatedAt} )")
    @SelectKey(statement = "SELECT @@IDENTITY", keyProperty = "oauth2GoogleId", before = false, resultType = long.class)
    void create(OAuth2GoogleRow oAuth2GoogleRow);

    @Update("UPDATE oauth2_google SET subject=#{subject}, idToken=#{idToken}, accessToken=#{accessToken}, issuedAt=#{issuedAt}, expiresAt=#{expiresAt}, updatedAt=#{updatedAt} WHERE subject = #{subject}")
    void update(OAuth2GoogleRow oAuth2GoogleRow);

    @Delete("DELETE FROM oauth2_google WHERE oauth2GoogleId = #{oauth2GoogleId}")
    void delete(@Param("oauth2GoogleId") long oauth2GoogleId);

    @Delete("DELETE FROM oauth2_google WHERE subject = #{subject}")
    void deleteBySubject(@Param("subject") String subject);
}

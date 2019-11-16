package masssh.boilerplate.spring.spa.dao;

import masssh.boilerplate.spring.spa.model.row.UserRow;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import java.util.Optional;

@Mapper
public interface UserDao {
    @Select("SELECT * FROM user WHERE userId = #{userId}")
    Optional<UserRow> single(@Param("userId") String userId);

    @Insert("INSERT INTO user VALUES ( #{userId}, #{userName}, #{role}, #{email}, #{locale}, #{passwordHash}, #{accessToken}, #{googleSubject} )")
    void create(UserRow userRow);

    @Update("UPDATE user SET userId=#{userId}, userName=#{userName}, role=#{role}, email=#{email}, locale=#{locale}, passwordHash=#{passwordHash}, accessToken=#{accessToken}, googleSubject=#{googleSubject} WHERE userId = #{userId}")
    void update(UserRow userRow);

    @Delete("DELETE FROM user WHERE userId = #{userId}")
    void delete(@Param("userId") String userId);

    @Select("SELECT * FROM user WHERE userName = #{userName}")
    Optional<UserRow> singleByUserName(@Param("userName") String userName);

    @Select("SELECT * FROM user WHERE googleSubject = #{subject}")
    Optional<UserRow> singleBySubject(@Param("subject") String subject);

    @Select("SELECT * FROM user WHERE googleSubject = #{email}")
    Optional<UserRow> singleByEmail(@Param("email") String email);

    @SelectProvider(type = SqlProvider.class)
    @ResultMap({"masssh.user-oauth2_google"})
    Optional<UserRow> singleOAuth2Detail(String subject);

    class SqlProvider implements ProviderMethodResolver {
        public String singleOAuth2Detail(@Param("subject") String subject) {
            return "" +
                           "SELECT " +
                           "  user.*, " +
                           "  oauth2_google.subject AS oauth2_google_subject, " +
                           "  oauth2_google.idToken AS oauth2_google_idToken, " +
                           "  oauth2_google.accessToken AS oauth2_google_accessToken, " +
                           "  oauth2_google.issuedAt AS oauth2_google_issuedAt, " +
                           "  oauth2_google.expiresAt AS oauth2_google_expiresAt " +
                           "FROM user " +
                           "JOIN oauth2_google ON user.googleSubject = oauth2_google.subject " +
                           "WHERE user.googleSubject = #{subject}";
        }
    }
}

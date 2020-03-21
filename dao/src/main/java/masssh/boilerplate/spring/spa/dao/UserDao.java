package masssh.boilerplate.spring.spa.dao;

import masssh.boilerplate.spring.spa.model.row.UserRow;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

@Mapper
public interface UserDao {
    @Select("SELECT * FROM user WHERE userId = #{userId}")
    Optional<UserRow> single(@Param("userId") long userId);

    @Select("SELECT * FROM user WHERE userHash = #{userHash}")
    Optional<UserRow> singleByUserHash(@Param("userHash") String userHash);

    @Insert("INSERT INTO user (userHash, userName, role, email, locale, passwordHash, accessToken, googleSubject, createdAt, updatedAt)" +
            "VALUES ( #{userHash}, #{userName}, #{role}, #{email}, #{locale}, #{passwordHash}, #{accessToken}, #{googleSubject}, #{createdAt}, #{updatedAt} )")
    @SelectKey(statement = "SELECT @@IDENTITY", keyProperty = "userId", before = false, resultType = long.class)
    int create(UserRow userRow) throws SQLIntegrityConstraintViolationException;

    @Update("UPDATE user SET userName=#{userName}, role=#{role}, locale=#{locale}, passwordHash=#{passwordHash}, accessToken=#{accessToken}, updatedAt=#{updatedAt} WHERE userId = #{userId}")
    int update(UserRow userRow);

    @Update("UPDATE user SET googleSubject=#{googleSubject}, updatedAt=#{updatedAt} WHERE userId = #{userId}")
    int updateGoogleSubject(UserRow userRow) throws SQLIntegrityConstraintViolationException;

    @Delete("DELETE FROM user WHERE userId = #{userId}")
    boolean delete(@Param("userId") long userId);

    @Select("SELECT * FROM user WHERE userName = #{userName}")
    Optional<UserRow> singleByUserName(@Param("userName") String userName);

    @Select("SELECT * FROM user WHERE googleSubject = #{subject}")
    Optional<UserRow> singleBySubject(@Param("subject") String subject);

    @Select("SELECT * FROM user WHERE email = #{email}")
    Optional<UserRow> singleByEmail(@Param("email") String email);

    @Select("SELECT * FROM user WHERE email = #{email} AND password = #{password}")
    Optional<UserRow> singleByEmailAndPassword(@Param("email") String email,
                                               @Param("password") String password);

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

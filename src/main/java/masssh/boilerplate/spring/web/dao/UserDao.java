package masssh.boilerplate.spring.web.dao;

import masssh.boilerplate.spring.web.model.row.UserRow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.Optional;

@Mapper
public interface UserDao {
    @Select("SELECT * FROM user WHERE userName = #{userName}")
    Optional<UserRow> single(final String userName);
}

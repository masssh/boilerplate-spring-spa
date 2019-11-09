package masssh.boilerplate.spring.spa.web.dao;

import masssh.boilerplate.spring.spa.web.model.row.SampleRow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.Optional;

@Mapper
public interface SampleDao {
    @Select("SELECT * FROM sample WHERE id = #{id}")
    Optional<SampleRow> singleSample(final int id);
}
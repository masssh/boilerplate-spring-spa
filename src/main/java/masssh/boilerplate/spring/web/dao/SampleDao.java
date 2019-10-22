package masssh.boilerplate.spring.web.dao;

import masssh.boilerplate.spring.web.model.row.SampleRow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.Optional;

@Mapper
public interface SampleDao {
    @Select("SELECT * FROM sample WHERE #{id}")
    Optional<SampleRow> singleSample(final int id);
}

package masssh.boilerplate.spring.spa.model.row;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SampleRow implements Serializable {
    private int id;
}

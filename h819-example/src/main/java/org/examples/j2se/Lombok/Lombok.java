package org.examples.j2se.Lombok;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/2/15
 * Time: 22:29
 * To change this template use File | Settings | File Templates.
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lombok {

    private String name;
    private Integer age;
    private Integer index;
}

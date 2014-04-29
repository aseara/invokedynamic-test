package indify;

import org.junit.Test;

/**
 *
 * @author John Rose <john.rose at sun.com>
 */
public class ExampleTest {
    @Test public void testMain() throws Throwable {
        System.out.println("main");
        Indify.main("--verify-specifier-count=1",
                "--verbose", "--overwrite",
                "--expand-properties", "--classpath", "${java.class.path}",
                "--java", Example.class.getName());
        //Example.main();
    }

}
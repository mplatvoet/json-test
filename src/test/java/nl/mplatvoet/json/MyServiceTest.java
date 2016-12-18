package nl.mplatvoet.json;

import org.junit.Assert;
import org.junit.Test;

import static nl.mplatvoet.json.JsonNodeMatcher.describesJson;
import static nl.mplatvoet.json.JsonUtil.toJsonNode;

public class MyServiceTest {
    private MyService service = new MyService();

    @Test
    public void full() throws Exception {
        String actual = service.createJson();

        Assert.assertThat("json must be described by", toJsonNode(actual), describesJson(MyServiceTest.class, "full.json"));
    }


}
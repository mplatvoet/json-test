package nl.mplatvoet.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.Is;
import org.junit.Assert;

import static nl.mplatvoet.json.JsonUtil.toJsonNode;

public class JsonNodeMatcher extends BaseMatcher<JsonNode> {
    private final JsonNode expected;

    private JsonNodeMatcher(JsonNode expected) {
        this.expected = expected;
    }

    public static Matcher<JsonNode> describesJson(Class<?> base, String fileName) {
        return new JsonNodeMatcher(toJsonNode(base, fileName));
    }

    public static Matcher<JsonNode> describesJson(String expected) {
        return new JsonNodeMatcher(toJsonNode(expected));
    }


    @Override
    public boolean matches(Object actual) {
        if (expected == null) {
            return actual == null;
        }

        if (!(actual instanceof JsonNode)) {
            return false;
        }
        //

        return matches((JsonNode) actual, expected);
    }

    private boolean matches(JsonNode actual, JsonNode expected) {
        if (actual.getNodeType() != expected.getNodeType()) return false;

        if (actual.isObject()) {
            return matchesObject(actual, expected);
        }
        if (actual.isArray()) {
            return matchesArray(actual, expected);
        }

        return actual.equals(expected);
    }

    private boolean matchesArray(JsonNode actual, JsonNode expected) {
        ImmutableList<JsonNode> actualNodes = ImmutableList.copyOf(actual.iterator());
        ImmutableList<JsonNode> expectedNodes = ImmutableList.copyOf(expected.iterator());

        if (actualNodes.size() != expectedNodes.size()) return false;

        UnmodifiableIterator<JsonNode> iterator = actualNodes.iterator();
        for (JsonNode expectedNode : expectedNodes) {
            JsonNode actualNode = iterator.next();
            if (!matches(actualNode, expectedNode)) {
                return false;
            }
        }
        return true;
    }

    private boolean matchesObject(JsonNode actual, JsonNode expected) {
        ImmutableSet<String> actualFieldNames = ImmutableSet.copyOf(actual.fieldNames());
        ImmutableSet<String> expectedFieldNames = ImmutableSet.copyOf(expected.fieldNames());

        for (String expectedFieldName : expectedFieldNames) {
            if (!actualFieldNames.contains(expectedFieldName)) {
                return false;
            }

            JsonNode expectedField = expected.get(expectedFieldName);
            JsonNode actualField = actual.get(expectedFieldName);
            if (!matches(actualField, expectedField)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("decribes ").appendValue(expected);
    }
}

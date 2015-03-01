package org.ztreamy;


import org.junit.Assert;
import org.junit.Test;

public class TestEvent {

    @Test
    public void testCreateUUID() {
        Assert.assertEquals(Event.createUUID().length(), 36);
    }

}

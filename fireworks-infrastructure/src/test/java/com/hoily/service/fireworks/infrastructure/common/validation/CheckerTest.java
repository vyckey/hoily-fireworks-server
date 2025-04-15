package com.hoily.service.fireworks.infrastructure.common.validation;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * description is here
 *
 * @author dongliu
 * 2023/9/3 20:26
 */
public class CheckerTest {
    @Test
    public void checkResultTest() {
        Assert.assertTrue(CheckResult.success().isSuccess());
        Assert.assertFalse(CheckResult.fail().isSuccess());

        Assert.assertEquals("suc", CheckResult.success("suc").getMessage());
        Assert.assertEquals("fail", CheckResult.fail("fail").getMessage());

        Assert.assertEquals(CheckResult.fail("fail"), CheckResult.of(false, "fail"));
        Assert.assertEquals(CheckResult.success("suc"), CheckResult.of(true, "%s", "suc", ""));

        Assert.assertEquals("suc", CheckResult.of(true, "suc", "fail").getMessage());
        Assert.assertEquals("fail", CheckResult.of(false, "suc", "fail").getMessage());

        CheckResult result1 = CheckResult.success().update(false, "fail");
        Assert.assertFalse(result1.isSuccess());
        Assert.assertEquals("fail", result1.getMessage());
        Assert.assertEquals(2, result1.getRecords().size());

        result1.update(true, "suc");
        Assert.assertTrue(result1.isSuccess());
        Assert.assertEquals("suc", result1.getMessage());
        Assert.assertEquals(3, result1.getRecords().size());
    }

    @Test
    public void checkerFactoryTest() {
        Assert.assertTrue(Checker.success().check(new Object()).isSuccess());
        Assert.assertFalse(Checker.fail("fail").check(new Object()).isSuccess());

        Checker<Object> checker1 = Checker.of(true, "suc");
        Assert.assertTrue(checker1.test(new Object()));
        Assert.assertEquals("suc", checker1.check(new Object()).getMessage());
        checker1 = Checker.of(false, "f");
        Assert.assertFalse(checker1.test(new Object()));
        Assert.assertEquals("f", checker1.check(new Object()).getMessage());

        Checker<Object> checker2 = Checker.withSucMsg(Objects::nonNull, "not null");
        Assert.assertFalse(checker2.test(null));
        Assert.assertNull(checker2.check(null).getMessage());
        Assert.assertTrue(checker2.test(new Object()));
        Assert.assertEquals("not null", checker2.check(new Object()).getMessage());

        Checker<Object> checker3 = Checker.withFailMsg(Objects::nonNull, "null obj");
        Assert.assertFalse(checker3.test(null));
        Assert.assertEquals("null obj", checker3.check(null).getMessage());
        Assert.assertTrue(checker3.test(new Object()));
        Assert.assertNull(checker3.check(new Object()).getMessage());
    }

    @Test
    public void checkerTest() {
        Checker<Object> checker1 = Checker.success("suc").andCheck(Objects::nonNull, "null obj");
        Assert.assertTrue(checker1.test(new Object()));
        Assert.assertNull(checker1.check(new Object()).getMessage());
        Assert.assertFalse(checker1.test(null));
        Assert.assertEquals("null obj", checker1.check(null).getMessage());

        Checker<Object> checker2 = Checker.fail("suc").orCheck(Objects::nonNull, "null obj");
        Assert.assertTrue(checker2.test(new Object()));
        Assert.assertEquals("null obj", checker2.check(new Object()).getMessage());
        Assert.assertFalse(checker2.test(null));
        Assert.assertNull(checker2.check(null).getMessage());

        Checker<Object> checker3 = Checker.success().negCheck("fail");
        Assert.assertFalse(checker3.test(new Object()));
        Assert.assertEquals("fail", checker3.check(new Object()).getMessage());

        Checker<Object> checker4 = Checker.fail("f").negCheck("suc");
        Assert.assertTrue(checker4.test(new Object()));
        Assert.assertEquals("suc", checker4.check(new Object()).getMessage());
    }

    @Test
    public void checkTest() {
        CheckResult result = CheckResult.success().andCheck("dongliu", Objects::isNull, "str isn't null");
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals("str isn't null", result.getMessage());

        CheckResult result2 = CheckResult.success().andCheck(1234, i -> (i & 0x1) == 0, "not a even number");
        Assert.assertTrue(result2.isSuccess());
        Assert.assertNull(result2.getMessage());

        CheckResult result3 = result2.andCheck(Arrays.asList(1, 2, 3), list -> list.size() == 3, "list size isn't 3")
                .andCheck(Arrays.asList("google", "apple", "huawei"), arr -> arr.contains("pinduoduo"), "not contains %s", "pdd");
        Assert.assertFalse(result3.isSuccess());
        Assert.assertEquals("not contains pdd", result3.getMessage());

        AtomicInteger times = new AtomicInteger();
        Checker<String> checker = Checker.withFailMsg(s -> {
            times.getAndIncrement();
            return s.matches("[a-z]");
        }, "not a upper case letters");
        result = CheckResult.success().andCheckAll(Arrays.asList("a", "b", "c", "d", "E", "f", "G"), checker);
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(5, times.get());
    }

    @Test
    public void fluentCheckTest() {
        CheckerTest instance = new CheckerTest();
        Checker<CheckerTest> checker1 = Checker.<CheckerTest>success()
                .andCheck(this::function2, "func2 fail");
        CheckResult checkResult1 = checker1.check(instance);
        Assert.assertTrue(checkResult1.isSuccess());

        CheckResult checkResult2 = checker1.andCheck(this::function1, "func1 fail")
                .andCheck(this::function2, "func3 fail")
                .andCheck(this::function2, "func4 fail")
                .check(instance);
        Assert.assertFalse(checkResult2.isSuccess());
        Assert.assertEquals("func1 fail", checkResult2.getMessage());

        Checker<CheckerTest> checker2 = checker1.orCheck(this::function4, "func4 fail");
        CheckResult checkResult3 = checker2.check(instance);
        Assert.assertTrue(checkResult3.isSuccess());

        CheckResult checkResult4 = checker2.negCheck("fail").check(instance);
        Assert.assertFalse(checkResult4.isSuccess());
        Assert.assertEquals("fail", checkResult4.getMessage());
    }

    private boolean function1(CheckerTest obj) {
        return false;
    }

    private boolean function2(CheckerTest obj) {
        return true;
    }

    public boolean function3(CheckerTest obj) {
        return true;
    }

    private boolean function4(CheckerTest obj) {
        return false;
    }
}
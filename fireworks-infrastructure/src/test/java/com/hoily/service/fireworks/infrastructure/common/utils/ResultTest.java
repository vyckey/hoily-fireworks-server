package com.hoily.service.fireworks.infrastructure.common.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.function.Function;

/**
 * {@link Result} test
 *
 * @author vyckey
 */
public class ResultTest {

    @Test
    public void baseTest() {
        Assert.assertTrue(Result.ok("success").isOk());
        Assert.assertTrue(Result.err("fail").isErr());

        Assert.assertEquals(Optional.of("200"), Result.ok("200").ok());
        Assert.assertEquals(Optional.empty(), Result.ok("200").err());
        Assert.assertEquals(Optional.of("500"), Result.err("500").err());
        Assert.assertEquals(Optional.empty(), Result.err("500").ok());

        Assert.assertEquals("value is null", Result.ok(null).notNullOr(() -> "value is null").unwrap());
        Assert.assertFalse(Result.err("fail").notNullOr(() -> "value is null").isOk());

        Result<String, String> hello = Result.ok("hello");
        Assert.assertEquals(Result.ok(5), hello.inspect(System.out::println).map(String::length));

        Result<String, String> err404 = Result.err("404");
        Assert.assertEquals(Result.err(404), err404.inspectErr(System.out::println).mapErr(Integer::parseInt));

        Assert.assertEquals(Integer.valueOf(100), err404.mapOr(String::length, 100));
        Assert.assertEquals(Integer.valueOf(0), err404.mapOrElse(String::length, e -> 0));

        Assert.assertEquals("hello", hello.expect("not hello"));
        Assert.assertEquals("404", err404.expectErr("not 404"));

        Assert.assertEquals("hello", hello.unwrap());
        Assert.assertEquals("404", err404.unwrapErr());

        Assert.assertEquals("200", err404.unwrapOr("200"));
        Assert.assertEquals("200", err404.unwrapOr(() -> "200"));
        Assert.assertEquals("status:404", err404.unwrapOrElse(e -> "status:" + e));

        Assert.assertEquals(Result.ok(1024), hello.and(Result.ok(1024)));
        Assert.assertEquals(err404, err404.and(Result.ok("1024")));
        Assert.assertEquals(Result.ok("hello world"), hello.andThen(s -> Result.ok(s + " world")));

        Assert.assertEquals(hello, hello.or(Result.ok("world")));
        Assert.assertEquals(Result.ok("world"), err404.or(Result.ok("world")));
        Assert.assertEquals(Result.err(404), err404.orElse(e -> Result.err(Integer.parseInt(e))));

        Assert.assertEquals(Result.ok("today"), Result.flatten(Result.ok(Result.ok("today"))));
        Assert.assertEquals(Result.err(Result.ok("abc")), Result.flatten(Result.err(Result.ok("abc"))));

        Assert.assertEquals(Optional.of(hello), hello.transpose());
        Assert.assertEquals(Optional.empty(), Result.ok(null).transpose());
        Assert.assertEquals(Optional.of(err404), err404.transpose());

        Assert.assertEquals("hello", hello.unwrap());
        Assert.assertEquals("404", err404.unwrapErr());

        Assert.assertEquals(Result.ok("cloned"), Result.ok("cloned").clone());
    }

    @Test(expected = IllegalStateException.class)
    public void unwrapTest() {
        Result.err(new Object()).unwrap();
    }

    @Test(expected = IllegalStateException.class)
    public void unwrapErrTest() {
        Result.ok(new Object()).unwrapErr();
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void unwrapErrMapTest() {
        Result<Object, ArrayIndexOutOfBoundsException> exception = Result.err(new ArrayIndexOutOfBoundsException());
        exception.unwrapOrThrow(Function.identity());
        throw exception.unwrapErr();
    }

}
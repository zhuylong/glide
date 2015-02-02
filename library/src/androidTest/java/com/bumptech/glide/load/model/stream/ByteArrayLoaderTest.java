package com.bumptech.glide.load.model.stream;

import static org.junit.Assert.assertNotNull;

import com.bumptech.glide.load.data.DataFetcher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.InputStream;

@RunWith(JUnit4.class)
public class ByteArrayLoaderTest {

    @Test
    public void testCanHandleByteArray() {
        ByteArrayLoader loader = new ByteArrayLoader();

        byte[] data = new byte[10];
        DataFetcher<InputStream> fetcher = loader.getDataFetcher(data, -1, -1);
        assertNotNull(fetcher);
    }
}
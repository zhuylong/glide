package com.bumptech.glide.gifdecoder;

import android.graphics.Bitmap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link com.bumptech.glide.gifdecoder.GifDecoder}.
 */
@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
public class GifDecoderTest {

    private MockProvider provider;

    @Before
    public void setUp() {
        provider = new MockProvider();
    }

    @Test
    public void testCanDecodeFramesFromTestGif() {
        byte[] data = TestUtil.readResourceData("partial_gif_decode.gif");
        GifHeaderParser headerParser = new GifHeaderParser();
        headerParser.setData(data);
        GifHeader header = headerParser.parseHeader();
        GifDecoder decoder = new GifDecoder(provider);
        decoder.setData(header, data);
        decoder.advance();
        Bitmap bitmap = decoder.getNextFrame();
        assertNotNull(bitmap);
        assertEquals(GifDecoder.STATUS_OK, decoder.getStatus());
    }

    @Test
    public void testFrameIndexStartsAtNegativeOne() {
        GifHeader gifheader = new GifHeader();
        gifheader.frameCount = 4;
        byte[] data = new byte[0];
        GifDecoder decoder = new GifDecoder(provider);
        decoder.setData(gifheader, data);
        assertEquals(-1, decoder.getCurrentFrameIndex());
    }

    @Test
    public void testAdvanceIncrementsFrameIndex() {
        GifHeader gifheader = new GifHeader();
        gifheader.frameCount = 4;
        byte[] data = new byte[0];
        GifDecoder decoder = new GifDecoder(provider);
        decoder.setData(gifheader, data);
        decoder.advance();
        assertEquals(0, decoder.getCurrentFrameIndex());
    }

    @Test
    public void testAdvanceWrapsIndexBackToZero() {
        GifHeader gifheader = new GifHeader();
        gifheader.frameCount = 2;
        byte[] data = new byte[0];
        GifDecoder decoder = new GifDecoder(provider);
        decoder.setData(gifheader, data);
        decoder.advance();
        decoder.advance();
        decoder.advance();
        assertEquals(0, decoder.getCurrentFrameIndex());
    }

    @Test
    public void testSettingDataResetsFramePointer() {
        GifHeader gifheader = new GifHeader();
        gifheader.frameCount = 4;
        byte[] data = new byte[0];
        GifDecoder decoder = new GifDecoder(provider);
        decoder.setData(gifheader, data);
        decoder.advance();
        decoder.advance();
        assertEquals(1, decoder.getCurrentFrameIndex());

        decoder.setData(gifheader, data);
        assertEquals(-1, decoder.getCurrentFrameIndex());
    }

    private static class MockProvider implements GifDecoder.BitmapProvider {

        @Override
        public Bitmap obtain(int width, int height, Bitmap.Config config) {
            Bitmap result = Bitmap.createBitmap(width, height, config);
            Robolectric.shadowOf(result).setMutable(true);
            return result;
        }

        @Override
        public void release(Bitmap bitmap) {
            // Do nothing.
        }
    }
}
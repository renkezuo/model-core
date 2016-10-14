package com.renke.core.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;

public class ImageUtil {

	// private static final Logger logger = LoggerFactory
	// .getLogger(ImageUtil.class);

	public static final String PNG = "png";
	public static final String JPG = "jpg";
	public static final String BMP = "bmp";
	public static final String GIF = "gif";

	public static byte[] readFromFile(String path) throws IOException {
		InputStream is = new FileInputStream(new File(path));
		byte[] buf = new byte[is.available()];
		is.read(buf);
		is.close();
		return buf;
	}

	public static ImageInfo getImageInfo(byte[] img) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(img);
		MemoryCacheImageInputStream is = new MemoryCacheImageInputStream(bais);
		Iterator<ImageReader> it = ImageIO.getImageReaders(is);
		ImageReader r = null;
		if (it.hasNext()) {
			r = (ImageReader) it.next();
		}

		if (r == null) {
			return null;
		}
		ImageInfo i = new ImageInfo();
		i.setType(r.getFormatName().toLowerCase());
		int index = r.getMinIndex();

		synchronized (r) {
			r.setInput(is);
			i.setHeight(r.getHeight(index));
			i.setWidth(r.getWidth(index));
		}
		return i;
	}

	public static String fastParseFileType(byte[] byte1) {
		if ((byte1[0] == 71) && (byte1[1] == 73) && (byte1[2] == 70) && (byte1[3] == 56) && ((byte1[4] == 55) || (byte1[4] == 57)) && (byte1[5] == 97)) {
			return ImageUtil.GIF;
		}
		if (((byte1[6] == 0X45) && (byte1[7] == 0X78) && (byte1[8] == 0X69) && (byte1[9] == 0X66)) 
				|| ((byte1[6] == 74) && (byte1[7] == 70) && (byte1[8] == 73) && (byte1[9] == 70))) {
			return ImageUtil.JPG;
		}
		if ((byte1[0] == 66) && (byte1[1] == 77)) {
			return ImageUtil.BMP;
		}
		if ((byte1[1] == 80) && (byte1[2] == 78) && (byte1[3] == 71)) {
			return ImageUtil.PNG;
		}
		return null;
	}

	public static class ImageInfo {
		private String type;
		private int width;
		private int height;

		public String getType() {
			return this.type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public int getWidth() {
			return this.width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return this.height;
		}

		public void setHeight(int height) {
			this.height = height;
		}
	}

	public enum ImageScaleType {

		/**
		 * 把图片不按比例扩大/缩小到View的大小显示
		 */
		fitXY() {
			public Image clipImage(BufferedImage source, Integer clipedWidth, Integer clipedHeight, int[] imageSizes) {
				imageSizes[0] = clipedWidth;
				imageSizes[1] = clipedHeight;
				return source.getScaledInstance(clipedWidth, clipedHeight, Image.SCALE_SMOOTH);
			}
		},
		/**
		 * 将图片的内容完整居中显示，通过按比例缩小或原来的size使得图片长/宽等于或小于View的长/宽
		 */
		centerInside() {
			public Image clipImage(BufferedImage source, Integer clipedWidth, Integer clipedHeight, int[] imageSizes) {
				// 1、等比例缩放
				int w = source.getWidth();
				int h = source.getHeight();
				double widthRate = clipedWidth.doubleValue() / w;
				double heightRate = clipedHeight.doubleValue() / h;
				if (widthRate <= heightRate) {
					imageSizes[0] = clipedWidth;
					imageSizes[1] = Double.valueOf(widthRate * h).intValue();

				} else {
					imageSizes[0] = Double.valueOf(heightRate * w).intValue();
					imageSizes[1] = clipedHeight;
				}
				// 2、按要求尺寸缩放
				return source.getScaledInstance(imageSizes[0], imageSizes[1], Image.SCALE_SMOOTH);
			}
		},
		/**
		 * 按图片的原来size居中显示，当图片长/宽超过View的长/宽，则截取图片的居中部分显示
		 */
		center() {
			public Image clipImage(BufferedImage source, Integer clipedWidth, Integer clipedHeight, int[] imageSizes) {
				imageSizes[0] = clipedWidth;
				imageSizes[1] = clipedHeight;
				Integer w = source.getWidth();
				Integer h = source.getHeight();
				double sourceRate = h.doubleValue() / w;
				double clipedRate = clipedHeight.doubleValue() / clipedWidth;

				int[] clipSourceimageSizes, subImagePoint;
				if (sourceRate <= clipedRate) {
					clipSourceimageSizes = new int[] { Double.valueOf(h / clipedRate).intValue(), h };
					subImagePoint = new int[] { (w - clipSourceimageSizes[0]) / 2, 0 };
				} else {
					clipSourceimageSizes = new int[] { w, Double.valueOf(clipedRate * w).intValue() };
					subImagePoint = new int[] { 0, (h - clipSourceimageSizes[1]) / 2 };
				}
				// source.getSubimage(subImagePoint[0], subImagePoint[1],
				// imageSizes[0], imageSizes[1]);

				// 2、按要求尺寸缩放
				return source.getSubimage(subImagePoint[0], subImagePoint[1], clipSourceimageSizes[0], clipSourceimageSizes[1]).getScaledInstance(clipedWidth, clipedHeight, Image.SCALE_SMOOTH);
			}
		};

		public abstract Image clipImage(BufferedImage source, Integer clipedWidth, Integer clipedHeight, int[] imageSizes);
	}

	public interface ImageSpecification {
		public Integer getWidth();

		public Integer getHeight();

		public Integer getCornerRadius();

		public Float getCompressQuality();

		public ImageScaleType getImageScaleType();

		public String getZoomedFileType(String originalFileType);
	}
}
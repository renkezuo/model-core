package com.renke.core.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.google.zxing.common.BitMatrix;

public class ImageTool {
	private static final int BLACK = 0xFF000000;// 用于设置图案的颜色
	private static final int WHITE = 0xFFFFFFFF; // 用于背景色
	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, (matrix.get(x, y) ? BLACK : WHITE));
				// image.setRGB(x, y,(matrix.get(x, y) ? Color.YELLOW.getRGB() :
				// Color.CYAN.getRGB()));
			}
		}
		return image;
	}
	public static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		// 设置logo图标
//		image = logoMatrix(image);
		if (!ImageIO.write(image, format, file)) {
			throw new IOException("Could not write an image of format " + format + " to " + file);
		} else {
			System.out.println("图片生成成功！");
		}
	}

	public static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		// 设置logo图标
//		image = logoMatrix(image);
		if (!ImageIO.write(image, format, stream)) {
			throw new IOException("Could not write an image of format " + format);
		}
	}
	
	public static BufferedImage logoMatrix(BufferedImage matrixImage) throws IOException {
		/**
		 * 读取二维码图片，并构建绘图对象
		 */
		Graphics2D g2 = matrixImage.createGraphics();
		int matrixWidth = matrixImage.getWidth();
		int matrixHeigh = matrixImage.getHeight();
		/**
		 * 读取Logo图片
		 */
		BufferedImage logo = ImageIO.read(new File("D:\\default.png"));
		// 开始绘制图片
		g2.drawImage(logo, matrixWidth / 5 * 2, matrixHeigh / 5 * 2, matrixWidth / 5, matrixHeigh / 5, null);// 绘制
		BasicStroke stroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(stroke);// 设置笔画对象
		// 指定弧度的圆角矩形
		RoundRectangle2D.Float round = new RoundRectangle2D.Float(matrixWidth / 5 * 2, matrixHeigh / 5 * 2,
				matrixWidth / 5, matrixHeigh / 5, 20, 20);
		g2.setColor(Color.white);
		g2.draw(round);// 绘制圆弧矩形
		// 设置logo 有一道灰色边框
		BasicStroke stroke2 = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(stroke2);// 设置笔画对象
		RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(matrixWidth / 5 * 2 + 2, matrixHeigh / 5 * 2 + 2,
				matrixWidth / 5 - 4, matrixHeigh / 5 - 4, 20, 20);
		g2.setColor(new Color(128, 128, 128));
		g2.draw(round2);// 绘制圆弧矩形
		g2.dispose();
		matrixImage.flush();
		return matrixImage;
	}
}

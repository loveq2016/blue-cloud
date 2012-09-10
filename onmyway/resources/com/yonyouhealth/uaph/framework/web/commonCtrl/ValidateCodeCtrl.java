package com.yonyouhealth.uaph.framework.web.commonCtrl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.yonyouhealth.uaph.framework.web.controller.BaseDomainCtrl;
import com.yonyouhealth.uaph.framework.web.controller.annotations.CTRL;
import com.yonyouhealth.uaph.framework.web.event.IReqData;
import com.yonyouhealth.uaph.framework.web.event.IResData;
import com.yonyouhealth.uaph.framework.web.event.UhwebRes;

/**
 * zzj:验证码的服务器端响应控制类，是后加如的
 */
@CTRL("ValidateCodeCtrl")
public class ValidateCodeCtrl extends BaseDomainCtrl {

	public static final String chose = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * 生成验证码
	 */
	public IResData genValidateCode(IReqData req) throws Exception {
		UhwebRes res = new UhwebRes();
		// 获取验证码长度
		int codeLength = new Integer((String) req.getAttr("codeLength"));
		// 获取前台验证码组件的名称
		String codeName = (String) req.getAttr("validateCodeName");
		// 获取前台图片的宽度
		int width = new Integer((String) req.getAttr("width"));
		// 获取前台图片的高度
		int height = new Integer((String) req.getAttr("height"));

		HttpServletRequest request = this.getHttpReq();
		HttpSession session = request.getSession();
		HttpServletResponse response = this.getHttpRes();
		response.setContentType("image/jpeg");
		ServletOutputStream sos = response.getOutputStream();

		// 设置浏览器不要缓存此图片
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		// 创建内存图象并获得其图形上下文
		int newWidth = width * codeLength + 6;
		BufferedImage image = new BufferedImage(newWidth, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();

		// 产生随机的认证码
		char[] rands = generateCheckCode(codeLength);

		// 产生图像
		drawBackground(g, newWidth, height, codeLength);
		drawRands(g, rands, width, height, codeLength);

		// 结束图像 的绘制 过程， 完成图像
		g.dispose();

		// 将图像输出到客户端
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(image, "JPEG", bos);
		byte[] buf = bos.toByteArray();
		response.setContentLength(buf.length);

		// 下面的语句也可写成： bos.writeTo(sos);
		sos.write(buf);
		bos.close();
		sos.close();

		// 将当前验证码存入到 Session 中
		session.setAttribute(codeName, new String(rands));

		return res;
	}

	private char[] generateCheckCode(int codeLen) {

		// 定义验证码的字符表
		String chars = "0123456789abcdefghijklmnopqrstuvwxyz";
		char[] rands = new char[codeLen];
		for (int i = 0; i < codeLen; i++) {
			int rand = (int) (Math.random() * 36);
			rands[i] = chars.charAt(rand);
		}
		return rands;

	}

	private void drawRands(Graphics g, char[] rands, int width, int height,
			int codeLen) {
		g.setColor(Color.BLACK);
		g.setFont(new Font(null, Font.ITALIC | Font.BOLD, 18 * width / 13));
		// 在不同的高度上输出验证码的每个字符
		for (int i = 0; i < codeLen; i++) {
			g.drawString("" + rands[i], width * i + 6, height - 4);
		}
	}

	private void drawBackground(Graphics g, int width, int height, int codeLen) {
		// 画背景
		g.setColor(new Color(0xDCDCDC));
		g.fillRect(0, 0, width, height);
		// 随机产生干扰点
		for (int i = 0; i < 30 * codeLen; i++) {
			int x = (int) (Math.random() * width);
			int y = (int) (Math.random() * height);
			int red = (int) (Math.random() * 255);
			int green = (int) (Math.random() * 255);
			int blue = (int) (Math.random() * 255);
			g.setColor(new Color(red, green, blue));
			g.drawOval(x, y, 1, 0);
		}
	}
}


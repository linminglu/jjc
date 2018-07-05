package com.framework.web.servlet;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author edgeloner edgeloner@163.com
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CheckCodeImage extends HttpServlet {

    //~ Static fields/initializers =============================================

    //==============================================================================

    /**
	 * 
	 */
	private static final long serialVersionUID = 7638409189194178899L;
	/**
     * log
     */
    private static Log log = LogFactory.getLog(CheckCodeImage.class);

    //~ Constructors ===========================================================

    /**
     * default contructor
     *
     */
    public CheckCodeImage() {

    }

    //~ Methods ================================================================

    //==============================================================================

    /**
     * service
     * @param request  aa
     * @param response aa
     * @exception IOException ioexception
     * @exception ServletException exception
     */
    public final void service(final HttpServletRequest request,
        final HttpServletResponse response)
        throws ServletException, IOException {
    	response.setContentType("image/jpeg");
        String checkCode = RandomStringUtils.randomNumeric(4);

        BufferedImage image = new BufferedImage(50, 18,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        Font font = new Font(g2d.getFont().getFontName(), Font.PLAIN, 18);
        g2d.setFont(font);
        g2d.setColor(Color.white);
        g2d.fill(new Rectangle(image.getWidth(), image.getHeight()));
        g2d.setColor(Color.darkGray);

        for (int i = 0; i < ((int) (Math.random() * 60) + 20); ++i) {

            g2d.fill(new Rectangle((int) (Math.random() * image.getWidth()),
                    (int) (Math.random() * image.getHeight()), 1, 1));
        }

        g2d.setPaint(Color.blue);
        g2d.drawString(checkCode, 2, 15); //(int) (Math.random() * 15) + 5
        g2d.dispose();

        //输出
        try {

            //ImageIO.write(image, "gif", response.getOutputStream());
        	OutputStream out = response.getOutputStream();
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.flush();
        } catch (Exception ex) {
        	if(log.isInfoEnabled()){
            log.info(ex);
        	}
        }

        request.getSession()
               .setAttribute("chkCode", checkCode);
    }
}

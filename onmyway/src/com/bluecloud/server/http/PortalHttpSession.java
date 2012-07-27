/**
 * 
 */
package com.bluecloud.server.http;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * @author leo
 *
 */
public class PortalHttpSession implements HttpSession {


	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getAttribute(java.lang.String)
	 */
	@Override
	public Object getAttribute(String arg0) {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getAttributeNames()
	 */
	@Override
	public Enumeration getAttributeNames() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getCreationTime()
	 */
	@Override
	public long getCreationTime() {
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getId()
	 */
	@Override
	public String getId() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getLastAccessedTime()
	 */
	@Override
	public long getLastAccessedTime() {
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getMaxInactiveInterval()
	 */
	@Override
	public int getMaxInactiveInterval() {
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getServletContext()
	 */
	@Override
	public ServletContext getServletContext() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getSessionContext()
	 */
	@Override
	public HttpSessionContext getSessionContext() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getValue(java.lang.String)
	 */
	@Override
	public Object getValue(String arg0) {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getValueNames()
	 */
	@Override
	public String[] getValueNames() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#invalidate()
	 */
	@Override
	public void invalidate() {
		

	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#isNew()
	 */
	@Override
	public boolean isNew() {
		
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#putValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public void putValue(String arg0, Object arg1) {
		

	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#removeAttribute(java.lang.String)
	 */
	@Override
	public void removeAttribute(String arg0) {
		

	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#removeValue(java.lang.String)
	 */
	@Override
	public void removeValue(String arg0) {
		

	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#setAttribute(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setAttribute(String arg0, Object arg1) {
		

	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#setMaxInactiveInterval(int)
	 */
	@Override
	public void setMaxInactiveInterval(int arg0) {
		

	}

}

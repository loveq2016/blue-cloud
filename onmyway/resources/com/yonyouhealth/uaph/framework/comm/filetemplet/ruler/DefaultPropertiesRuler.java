package com.yonyouhealth.uaph.framework.comm.filetemplet.ruler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yonyouhealth.uaph.framework.comm.filetemplet.FileTempletException;
import com.yonyouhealth.uaph.framework.comm.filetemplet.ITempFile;
import com.yonyouhealth.uaph.framework.comm.filetemplet.tempfile.StringTempFile;
import com.yonyouhealth.uaph.framework.comm.log.LogFactory;
import com.yonyouhealth.uaph.framework.comm.log.LogWritter;

public class DefaultPropertiesRuler extends AbsRuler
{
  private static final LogWritter logger = LogFactory.getLogger(DefaultPropertiesRuler.class);

  public Object make(ITempFile tempfile)
  {
    Map templet = null;
    try {
      templet = (Map)tempfile.getTempContent();
    } catch (ClassCastException ex) {
      throw new FileTempletException("703", DefaultPropertiesRuler.class.getName(), tempfile.getClass().getName());
    }

    Map result = new HashMap();

    for (Iterator iter = getMyParams().keySet().iterator(); iter.hasNext(); ) {
      String pname = (String)iter.next();
      if (templet.containsKey(pname)) {
        result.put(pname, dealWithStringRuler((String)templet.get(pname), getMyParams().get(pname)));
      }
    }

    return result;
  }

  public Object dealWithStringRuler(String templet, Object params) {
    if (List.class.isAssignableFrom(params.getClass()))
    {
      ITempFile tf = new StringTempFile();
      List contents = new ArrayList();
      contents.add(templet);
      tf.setContentList(contents);

      DefaultStringRulerList rlp = new DefaultStringRulerList();
      rlp.setParams((List)params);
      return rlp.make(tf);
    }
    if (Map.class.isAssignableFrom(params.getClass())) {
      ITempFile tf = new StringTempFile();
      List contents = new ArrayList();
      contents.add(templet);
      tf.setContentList(contents);

      DefaultStringRulerMap rmp = new DefaultStringRulerMap();
      rmp.setParams((Map)params);
      return rmp.make(tf);
    }

    throw new FileTempletException("702", params.getClass().getName());
  }

  public Map getMyParams()
  {
    try
    {
      return (Map)getParams();
    } catch (ClassCastException ex) {
      throw new FileTempletException("704", DefaultPropertiesRuler.class.getName(), getParams().getClass().getName());
    }
  }

  private void debug(String str)
  {
    logger.debug(str);
  }

  public static void main(String[] args) {
    DefaultPropertiesRuler aaa = new DefaultPropertiesRuler();
    System.out.println("1---->����LIst");
    aaa.dealWithStringRuler(null, new ArrayList());
    System.out.println("2---->����Map");
    aaa.dealWithStringRuler(null, new HashMap());
    System.out.println("3---->��������");
  }
}
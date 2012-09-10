package com.yonyouhealth.uaph.framework.comm.filetemplet.ruler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyouhealth.uaph.framework.comm.filetemplet.FileTempletException;
import com.yonyouhealth.uaph.framework.comm.filetemplet.ITempFile;
import com.yonyouhealth.uaph.framework.comm.util.StringUtils;

public class DefaultStringRulerList extends AbsStringRuler
{
  public Object make(ITempFile tempfile)
  {
    try
    {
      String templet = (String)tempfile.getTempContent();
      return makeContent(templet, (List)getParams());
    }
    catch (ClassCastException ex) {
      throw new FileTempletException("703", DefaultStringRulerList.class.getName(), tempfile.getClass().getName());
    }
  }

  public String makeContent(String original, List param)
  {
    Map flagMap = new HashMap();

    int iStart = 0;

    while ((iStart = original.indexOf("{", iStart + 1)) != -1) {
      int iEnd = original.indexOf("}", iStart);
      String sChar = original.substring(iStart - 1, iStart + 1);
      char aChar = original.charAt(iStart);
      char aChar1 = original.charAt(iStart + 1);
      char aChar2 = original.charAt(iStart - 1);
      char aChar3 = original.charAt(iStart - 2);
      if (aChar != '\\') {
        String resCode = original.substring(iStart + 1, iEnd);
        flagMap.put("s" + resCode, new Integer(iStart));
        flagMap.put("e" + resCode, new Integer(iEnd));
      }

    }

    for (int i = param.size() - 1; i >= 0; --i) {
      String resValue = (String)param.get(i);
      if (flagMap.get("s" + (i + 1)) != null) {
        int i_Start = ((Integer)flagMap.get("s" + (i + 1))).intValue();
        int i_End = ((Integer)flagMap.get("e" + (i + 1))).intValue();
        original = StringUtils.replaceByPos(original, i_Start, i_End, resValue);
      }
    }

    return original;
  }
}
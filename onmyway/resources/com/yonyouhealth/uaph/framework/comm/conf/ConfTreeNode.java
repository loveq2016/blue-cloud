package com.yonyouhealth.uaph.framework.comm.conf;

public class ConfTreeNode
{
  private ConfTreeNode parentNode;
  private String nodeName;
  private boolean isLeaf;
  private String nodeContent;
  private String loaderClassName;
  private String language;
  private String applied;

  public ConfTreeNode()
  {
  }

  public ConfTreeNode(String parent, String nodeType, String nodeContent, Integer nodeIndex)
  {
  }

  public String getPrimaryKey()
  {
    if (this.parentNode != null) {
      return this.parentNode.getPrimaryKey() + "." + this.nodeName;
    }
    return this.nodeName;
  }

  public void setLoaderClassName(String loaderClassName)
  {
    this.loaderClassName = loaderClassName;
  }

  public String getLoaderClassName() {
    return this.loaderClassName;
  }

  public void setNodeContent(String nodeContent) {
    this.nodeContent = nodeContent;
  }

  public String getNodeContent() {
    return this.nodeContent;
  }

  public void setNodeName(String nodeName) {
    this.nodeName = nodeName;
  }

  public String getNodeName() {
    return this.nodeName;
  }

  public void setParentNode(ConfTreeNode parentNode) {
    this.parentNode = parentNode;
  }

  public ConfTreeNode getParentNode() {
    return this.parentNode;
  }

  public void setApplied(String applied) {
    this.applied = applied;
  }

  public String getApplied() {
    return this.applied;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getLanguage() {
    return this.language;
  }

  public boolean isLeaf() {
    return this.isLeaf;
  }
  public void setLeaf(boolean isLeaf) {
    this.isLeaf = isLeaf;
  }
}
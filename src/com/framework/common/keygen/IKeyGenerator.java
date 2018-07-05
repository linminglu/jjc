package com.framework.common.keygen;

/**
 * The interface specifies a contract for various strategies of unique id
 * generation. All implementations for key generations must implement this
 * interface.
 *
 * @author Neelesh
 * @version 1.0

 */
public interface IKeyGenerator {
  /**
   * Returns a globally unique id.
   *
   * @return <b>String</b> unique id
   */
  public String getKey();
}


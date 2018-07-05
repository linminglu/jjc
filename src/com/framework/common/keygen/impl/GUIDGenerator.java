package com.framework.common.keygen.impl;

// Import to generate random number
import java.security.SecureRandom;

import com.framework.common.keygen.IKeyGenerator;


/**
 * This class uses a combination of current time in milliseconds and a random

 * number to generate a unique key.
 *
 * @author Neelesh
 * @version 1.0
 */
public class GUIDGenerator implements IKeyGenerator {

  // Instance of secure random to generate random numbers
  private SecureRandom random;


  /**
   * Constructor. An instance of SecureRandom is initialized here.
   */
  public GUIDGenerator() {
    random = new SecureRandom();
    
  }

  /**

   * This method returns a key that is unique to a millisecond. The uniqueness
   * is increased by appending a random number to the key.
   *
   * @return <b>String</b> The unique key
   */
  public String getKey() {

    // Append current time in millis to a hexadecimal representation
    // of a random number.
    String key =
      "" + System.currentTimeMillis() + Long.toHexString(random.nextInt());


    return key;
  }
}

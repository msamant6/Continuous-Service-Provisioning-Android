/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.yelpdb;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

/**
 *
 * @author rmurali7
 */
public class TwoStepOAuth extends DefaultApi10a {
    
  @Override
  public String getAccessTokenEndpoint() {
    return null;
  }

  @Override
  public String getAuthorizationUrl(Token arg0) {
    return null;
  }

  @Override
  public String getRequestTokenEndpoint() {
    return null;
  }
    
}

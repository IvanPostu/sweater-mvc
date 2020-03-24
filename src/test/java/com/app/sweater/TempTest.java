package com.app.sweater;

import com.app.sweater.application.controller.ControllerUtils;
import org.junit.Test;

public class TempTest {
  @Test
  public void test(){

    int z= 9 ;

    int[] result1 = ControllerUtils.calculatePages(6, 11, 6);
    int[] result2 = ControllerUtils.calculatePages(61, 11, 6);
    int[] result3 = ControllerUtils.calculatePages(6, 11, 61);
    int[] result4 = ControllerUtils.calculatePages(6, 0, 6);
    int[] result5 = ControllerUtils.calculatePages(6, 61, 6);
    int[] result6 = ControllerUtils.calculatePages(2, 11, 2);
    int[] result7 = ControllerUtils.calculatePages(2, 11, 61);
    int[] result8 = ControllerUtils.calculatePages(61, 11, 2);
    int[] result9 = ControllerUtils.calculatePages(61, 11, 0);


    char a ='a';

  }

}

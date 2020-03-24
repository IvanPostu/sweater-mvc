package com.app.sweater.application.controller;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ControllerUtils {


  public static Map<String, String> getErrors(BindingResult bindingResult) {
    Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
        fieldError -> fieldError.getField() + "Error",
        DefaultMessageSourceResolvable::getDefaultMessage
    );

    return bindingResult.getFieldErrors().stream().collect(collector);
  }


  public static int[] calculatePages(final int maxDiapason, final int currentPage, final int totalPages) {

    if (maxDiapason >= totalPages) {
      int[] result = new int[totalPages];
      for (int i = 0; i < totalPages; i++) result[i] = i;
      return result;
    }

    LinkedList<Integer> result = new LinkedList<>();
    result.addFirst(currentPage);

    for (int i = 0, j = 1; i < maxDiapason-1; j++) {
      if (currentPage + j < totalPages) {
        i++;
        result.addLast(currentPage + j);
      }
      if (currentPage - j >= 0) {
        i++;
        result.addFirst(currentPage - j);
      }
      if (!(currentPage - j >= 0) && !(currentPage + j < totalPages)) {
        int[] result1 = new int[totalPages];
        for (int z = 0; z < totalPages; z++) result1[z] = z;
        return result1;
      }

    }

    int resultIndex = 0;
    int[] resultArray = new int[result.size()];

    for (Integer item : result) {
      resultArray[resultIndex] = item.intValue();
      resultIndex++;
    }


    return resultArray;
  }



}

package com.dyzs.common.examples;

import com.dyzs.common.component.TestAnnotation;

public interface IAnnotationExplainer {

    void valueOfGender(@TestAnnotation(value = {"girl", "boy"}) String gender);


}

package eu.heliosteam.heliosumlgen.javaModel.checks;

import eu.heliosteam.heliosumlgen.JsonConfig;
import eu.heliosteam.heliosumlgen.javaModel.JavaModel;

import java.util.List;

public interface IPatternCheck {
    List<IPattern> check(JavaModel model);

}

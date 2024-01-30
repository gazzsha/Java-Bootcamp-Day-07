package com.school21.application;

import com.google.auto.service.AutoService;
import com.school21.annotations.HtmlForm;
import com.school21.annotations.HtmlInput;
import org.checkerframework.checker.units.qual.C;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@SupportedAnnotationTypes("com.school21.annotations.*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class HtmlProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(HtmlForm.class);

            for (Element element : annotatedElements) {
                try {
                    writeBuilderFile(element);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
    }

    private void writeBuilderFile(Element element) throws IOException {

        final String htmlAction = "<form action = \"%s\" method = \"%s\">\n";
        final String htmlInput =" <input type = \"%s\" name = \"%s\" placeholder = \"%s\">\n";
        final String htmlInputType = "<input type = \"submit\" value = \"Send\">";
        final String endForm = "</form>";
        HtmlForm htmlForm =  element.getAnnotation(HtmlForm.class);
        try (FileWriter out = new FileWriter("./target/classes/" + htmlForm.fileName())) {
            out.write(String.format(htmlAction,htmlForm.action(),htmlForm.action(),htmlForm.method()));
            for (Element elem : element.getEnclosedElements()) {
                HtmlInput input = elem.getAnnotation(HtmlInput.class);
                if (input != null) out.write(String.format(htmlInput,input.type(),input.name(),input.placeholder()));
            }
            out.write(htmlInputType);
            out.write(endForm);
            out.flush();
        }
    }
}

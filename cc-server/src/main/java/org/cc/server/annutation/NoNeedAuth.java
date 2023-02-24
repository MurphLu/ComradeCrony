package org.cc.server.annutation;

import javax.xml.stream.events.EndElement;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoNeedAuth {
}

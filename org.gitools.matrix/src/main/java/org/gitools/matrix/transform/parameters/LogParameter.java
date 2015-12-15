package org.gitools.matrix.transform.parameters;


public class LogParameter extends AbstractFunctionParameter<LogParameter.LogEnum> {

    @Override
    public boolean validate(LogParameter.LogEnum parameter) {
        return super.validate(parameter);
    }

    public enum LogEnum {
        baseN("Log Naturalis"), base10("Log10"),  base2("Log2"), custom("Custom");

        private String name;

        LogEnum(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}



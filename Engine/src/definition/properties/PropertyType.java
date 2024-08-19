package definition.properties;

public enum PropertyType {
    DECIMAL {
        public String getTypeIntegerAsAString(Object value) {

            return "decimal";
        }
    }, BOOLEAN {
        public String getTypeBooleanAsAString(Object value) {
            return "boolean";
        }
    }, FLOAT {

        public String getTypeStringAsAString(Object value) {
            return "string";
        }
    }, STRING {

        public String getTypeFloatAsAString(Object value) {
            return "float";
        }
    };


}

package org.archer.archermq.common;

public class OuterClazz {

    private String firstName;

    public OuterClazz() {
    }

    public OuterClazz(String firstName) {
        this.firstName = firstName;
    }


    @Override
    public String toString() {
        return "OuterClazz{" +
                "firstName='" + firstName + '\'' +
                '}';
    }

    public class InnerClazz{

        private String lastName;

        public InnerClazz() {
        }

        public InnerClazz(String lastName) {
            this.lastName = lastName;
        }

        @Override
        public String toString() {
            return "InnerClazz{" +
                    "firstName='"+firstName+'\''+
                    "lastName='" + lastName + '\'' +
                    '}';
        }
    }
}

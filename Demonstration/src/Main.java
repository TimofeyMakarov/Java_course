public class Main {

    public final class PhoneNumber {
        private final short areaCode;
        private final short exchange;
        private final short extension;
        PhoneNumber(short areaCode, short exchange, short extension){
            this.areaCode = areaCode;
            this.exchange = exchange;
            this.extension = extension;
        }
        public boolean equals(Object o) {
            if (o==this) return true; // рефлексивность
            if (!(o instanceof PhoneNumber)) return false; // Оператор instanceof возвращает true,
            // если объект является экземпляром класса или функции-конструктора,
            // и false в противном случае.
            // То есть мы проверяем, является ли o объектом класса PhoneNumber:
            // если объекты из разных классов, очеивдно, что они не равны
            PhoneNumber pn = (PhoneNumber)o;               // преобразование к типу PhoneNumber (см лекцию 7)
            return pn.extension == extension &&
                    pn.exchange == exchange && pn.areaCode == areaCode;
        }
        public int hashCode() {
            int result = 17;
            result = 31 * result + areaCode;
            result = 31 * result + prefix;
            result = 31 * result + lineNumber;
            return result;
        }
    }



    public static void main(String[] args) {
        Runtime a = Runtime.getRuntime();
        a.exit(0);
    }
}



public class Sprzet {
    public String nazwa;
    public String typ;
    public String producent;
    public int ilosc_na_stanie;
    public float koszt;

    public Sprzet(){
        setName(nazwa);
        setType(typ);
        setProducent(producent);
        setIlosc(ilosc_na_stanie);
        setKoszt(koszt);
    }

    public String getName() {
        return nazwa;
    }

    public void setName(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getType() {
        return typ;
    }

    public void setType(String typ) {this.typ = typ;}

    public String getProducent() {
        return producent;
    }

    public void setProducent(String producent) {
        this.producent = producent;
    }

    public int getIlosc() {return ilosc_na_stanie;}

    public void setIlosc(int ilosc_na_stanie) {this.ilosc_na_stanie = ilosc_na_stanie; }

    public float getKoszt() {return koszt;}

    public void setKoszt(float koszt) {this.koszt = koszt;}
}


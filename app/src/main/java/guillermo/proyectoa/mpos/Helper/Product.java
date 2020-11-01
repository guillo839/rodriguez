package guillermo.proyectoa.mpos.Helper;

public class Product {
    private String Codigo;
    private String Producto;
    private double Precio;
    private int img;

    public Product (String Producto,double Precio, int img)
    {
        this.Producto=Producto;
        this.Precio =Precio;
        this.img =img;
    }

    public String getProducto() {
        return Producto;
    }

    public void setProducto(String prod) {
        Producto = prod;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String cod) {
        Producto = cod;
    }

    public double getPrecio() {
        return Precio;
    }

    public void setPrecio(double precio) {
        Precio = precio;
    }

    public int getImage() {
        return img;
    }

    public void setPrecio(int imagen) {
        img = imagen;
    }
}

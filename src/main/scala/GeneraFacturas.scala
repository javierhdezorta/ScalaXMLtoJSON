import java.io.File

import com.github.tototoshi.csv.CSVReader

import scala.xml.{Elem, Node, XML}

object GeneraFacturas {

  var check="z"
  var facturaTemp= <name>Javier</name>
  var facturaFinal: Node= <name>Orta</name>
  var conceptos: Node= <name>Orta</name>


  def main(args: Array[String]): Unit = {
    readCSV()
  }



  def readCSV()={
    val reader = CSVReader.open(new File("/home/javier/PycharmProjects/DataSetFacturas/data.csv"))

    var rows=reader.allWithHeaders()

    var datos: List[String] = List()

    for (row <- rows){
      var InvoiceNo=row("InvoiceNo").toString
      var StockCode=row("StockCode").toString
      var Description=row("Description").toString
      var Quantity=row("Quantity").toString
      var UnitPrice=row("UnitPrice").toString



      if(check!=InvoiceNo){

        var InvoiceDate=row("InvoiceDate").toString
        var CustomerID=row("CustomerID").toString
        var Country=row("Country").toString

        if (datos.nonEmpty){
          //println(datos)
          writeXML(InvoiceNo,InvoiceDate,CustomerID,Country,datos)
          datos=List()
        }
        var data=InvoiceNo+"="+ Quantity+"="+StockCode+"="+Description+"="+UnitPrice
        datos=data::datos
      }
      else {
        var data=InvoiceNo+"="+Quantity+"="+StockCode+"="+Description+"="+UnitPrice
        datos=data::datos
      }
      check=InvoiceNo
    }
  }

  def writeXML(InvoiceNo:String,InvoiceDate:String,CustomerID:String,Country:String,datos:List[String])={

    def addNode(to: Node, newNode: Node) = to match {
      case Elem(prefix, label, attributes, scope, child@_*) => Elem(prefix, label, attributes, scope, child ++ newNode: _*)
      case _ => println("could not find node"); to
    }

    var postName:String=""

    facturaTemp = <cfdi:Comprobante
    xmlns:cfdi="http://www.sat.gob.mx/cfd/3"
    xmlns:tfd="http://www.sat.gob.mx/TimbreFiscalDigital"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation=" http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv32.xsd " serie="F" version="3.2" folio={InvoiceNo} fecha="2016-11-22T13:49:53" sello="EmITWbuW3JdXNdMVm8mUsY0g1GsJbXyUZfcAX6bxibNggXHOBalnJjWlRzcEqMZyZdG1HiHEZFf1hXK6ABkC0vF/urSkKXwzX/if2ZTET++9xHQyqPJzgfYJU4Mp5rFW2vtxBybpq5a0Z4+/AA2wSMDH+q2W0OG5qHv+NSLfdto=" tipoDeComprobante="ingreso" formaDePago="Pago en una sola exhibición" noCertificado="00001000000305104410" certificado="MIIEgTCCA2mgAwIBAgIUMDAwMDEwMDAwMDAzMDUxMDQ0MTAwDQYJKoZIhvcNAQEFBQAwggGKMTgwNgYDVQQDDC9BLkMuIGRlbCBTZXJ2aWNpbyBkZSBBZG1pbmlzdHJhY2nDs24gVHJpYnV0YXJpYTEvMC0GA1UECgwmU2VydmljaW8gZGUgQWRtaW5pc3RyYWNpw7NuIFRyaWJ1dGFyaWExODA2BgNVBAsML0FkbWluaXN0cmFjacOzbiBkZSBTZWd1cmlkYWQgZGUgbGEgSW5mb3JtYWNpw7NuMR8wHQYJKoZIhvcNAQkBFhBhY29kc0BzYXQuZ29iLm14MSYwJAYDVQQJDB1Bdi4gSGlkYWxnbyA3NywgQ29sLiBHdWVycmVybzEOMAwGA1UEEQwFMDYzMDAxCzAJBgNVBAYTAk1YMRkwFwYDVQQIDBBEaXN0cml0byBGZWRlcmFsMRQwEgYDVQQHDAtDdWF1aHTDqW1vYzEVMBMGA1UELRMMU0FUOTcwNzAxTk4zMTUwMwYJKoZIhvcNAQkCDCZSZXNwb25zYWJsZTogQ2xhdWRpYSBDb3ZhcnJ1YmlhcyBPY2hvYTAeFw0xNDA5MTIxOTE2MzBaFw0xODA5MTIxOTE2MzBaMIHNMSswKQYDVQQDEyJBTEJFUlRPIEZSQU5DSVNDTyBQQVNBUkFOIEZJR1VFUk9BMSswKQYDVQQpEyJBTEJFUlRPIEZSQU5DSVNDTyBQQVNBUkFOIEZJR1VFUk9BMSswKQYDVQQKEyJBTEJFUlRPIEZSQU5DSVNDTyBQQVNBUkFOIEZJR1VFUk9BMRYwFAYDVQQtEw1QQUZBODIxMDA1SU44MRswGQYDVQQFExJQQUZBODIxMDA1SE1DU0dMMDMxDzANBgNVBAsTBnVuaWRhZDCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAhg68ZOyj2vxfp2D9phYNlaxTLI0vyx4Qn7k82CFrRZ1nJKWSl+3+o8aZzsdr0pTdwdM4DMquQtj8pgsaUqdKpRn2qYFjj+gAMR9Bet1h08122xIex1wp4OWeKpo5W9HSrWulC/de2lMC1OqxG/3c1BbJ1OnsamFPx/DgAdPNpa0CAwEAAaMdMBswDAYDVR0TAQH/BAIwADALBgNVHQ8EBAMCBsAwDQYJKoZIhvcNAQEFBQADggEBAEyMt38i5pX168IFCNY0aLOeM06HMimq+aeZlcYKxqjtE02tg3FnxJVKsfaotDUhlqG+WDgUQ2Xw2U355t5craVHONrU9efu6mZ0x1IZ3YTcZlmgylyToruoHZd4vACzoDqwZ9/tg2U5LK0g8rNiVSH8jo3G9F4dzYGXHaRnnOz2yXP5wkTSON0SnFPwb7e5vewGgXbT0DNFL7lk9dI3peXpaGevOLRrQ0f0+4KReAH27Tq3rbUzXQnzQHmnmJjbOY18ari7DD9/HwfdNa9PRcotLgcMvTeZRlzT/OY2xD1PhVWmbmym2S8uLhODckRvqSti7iaWl3/oYHFvLYot00c=" condicionesDePago="Contado" subTotal="1724.12" descuento="0.00" TipoCambio="1.0000" Moneda="MXN" total="1999.98" metodoDePago="01" LugarExpedicion="Azcapotzalco, D.F.">
      <cfdi:Emisor rfc="PAFA821005IN8" nombre="ALBERTO FRANCISCO PASARAN FIGUEROA">
        <cfdi:DomicilioFiscal calle="AURORA" noExterior="6 BIS" colonia="Santa Ines" localidad="México" municipio="Azcapotzalco" estado="D.F." pais="Mexico" codigoPostal="02140"/>
        <cfdi:ExpedidoEn calle="AURORA" noExterior="6 BIS" colonia="Santa Ines" municipio="Azcapotzalco" estado="D.F." pais="Mexico" codigoPostal="02140"/>
        <cfdi:RegimenFiscal Regimen="Regímen de Incorporación Fiscal"/>
      </cfdi:Emisor>
      <cfdi:Receptor rfc={CustomerID} nombre="INOXCRAP.S.A.DE.C.V">
        <cfdi:Domicilio calle="LAGO MAYOR" noExterior="34" colonia="ANAHUAC" localidad="México" municipio="Miguel Hidalgo" estado="D.F." pais="Mexico" codigoPostal="11320"/>
      </cfdi:Receptor>
    </cfdi:Comprobante>

    conceptos= <cfdi:Conceptos>
    </cfdi:Conceptos>

    for (dato <- datos){
      var array=dato.split("=")
      postName=array(0)
      var concepto= <cfdi:Concepto cantidad={array(0)} unidad={array(2)} descripcion={array(3)} valorUnitario={array(4)} importe="1724.12">
        <cfdi:ComplementoConcepto/>
      </cfdi:Concepto>
      var temp=addNode(conceptos,concepto)
      conceptos=temp
    }

    facturaFinal= addNode(facturaTemp,conceptos)

    var impuestos= <cfdi:Impuestos totalImpuestosTrasladados="275.86">
      <cfdi:Traslados>
        <cfdi:Traslado impuesto="IVA" tasa="16" importe="275.86"/>
      </cfdi:Traslados>
    </cfdi:Impuestos>

    facturaFinal= addNode(facturaFinal,impuestos)

    var comp= <cfdi:Complemento>
      <tfd:TimbreFiscalDigital
      xmlns:tfd="http://www.sat.gob.mx/TimbreFiscalDigital" xsi:schemaLocation="http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/TimbreFiscalDigital/TimbreFiscalDigital.xsd" version="1.0" UUID="B074D137-0EF6-4A9F-8426-7457A4A681E3" FechaTimbrado="2016-11-22T13:48:21" selloCFD="EmITWbuW3JdXNdMVm8mUsY0g1GsJbXyUZfcAX6bxibNggXHOBalnJjWlRzcEqMZyZdG1HiHEZFf1hXK6ABkC0vF/urSkKXwzX/if2ZTET++9xHQyqPJzgfYJU4Mp5rFW2vtxBybpq5a0Z4+/AA2wSMDH+q2W0OG5qHv+NSLfdto=" noCertificadoSAT="00001000000301100488" selloSAT="QROEsvEgM1JpNNRsO8weFAgyfqeyxHqnNxjD2eev30Imde5bhGshwixH9YvCS7P2uKB6W+dmrPiijxTisMk/mbehNb2FkqP6E1TZZSCxxKjVsBnjImzlT9L7klMhQ7vlTysrilWQpZAP+y927hBu44MSHH7k6U6k10lzjpM5/4g="/>
    </cfdi:Complemento>

    facturaFinal=addNode(facturaFinal,comp)
    println(facturaFinal)
    XML.save("/home/javier/facturas/FACTURA_F_"+postName+".xml", facturaFinal , "UTF-8", true, null)
  }
}
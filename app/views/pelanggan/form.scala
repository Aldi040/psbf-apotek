@(form: Form[(String, String)], id: Option[Int] = None)(implicit request: RequestHeader, messages: Messages)

@main(if(id.isDefined) "Edit Pelanggan" else "Tambah Pelanggan") {
  <h1>@if(id.isDefined) { Edit Pelanggan } else { Tambah Pelanggan Baru }</h1>
  
  @helper.form(if(id.isDefined) { routes.PelangganController.update(id.get) } 
               else { routes.PelangganController.save }) {
    @helper.CSRF.formField
    
    @helper.inputText(form("nama"), '_label -> "Nama Pelanggan")
    
    @helper.select(
      form("jenis_kelamin"),
      options = Seq("Laki-Laki" -> "Laki-Laki", "Perempuan" -> "Perempuan"),
      '_label -> "Jenis Kelamin"
    )
    
    <button type="submit">Simpan</button>
  }
}
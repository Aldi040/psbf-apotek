@(pelanggans: List[models.Pelanggan])(implicit request: RequestHeader, messages: Messages)

@main("Daftar Pelanggan") {
  <h1>Daftar Pelanggan</h1>
  
  <a href="@routes.PelangganController.createForm">Tambah Baru</a>

  <table>
    <thead>
      <tr>
        <th>ID</th>
        <th>Nama</th>
        <th>Jenis Kelamin</th>
        <th>Aksi</th>
      </tr>
    </thead>
    <tbody>
    @for(p <- pelanggans) {
      <tr>
        <td>@p.id</td>
        <td>@p.nama</td>
        <td>@p.jenisKelamin</td>
        <td>
          <a href="@routes.PelangganController.editForm(p.id)">Edit</a>
          <form method="POST" action="@routes.PelangganController.delete(p.id)">
            <button type="submit">Hapus</button>
          </form>
        </td>
      </tr>
    }
    </tbody>
  </table>
}
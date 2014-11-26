		  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
		  
		  <script type="text/javascript">
		    $(document).ready(function(){
		    	$('#btn-cari-pegawai').click(function(){
		    		$('#page').val(0);
		    		cariPegawai();
		    	});
		    	
		    	$('#btn-next').click(function(){
		    		var page = $('#page').val();
		    		page++;
		    		$('#page').val(page);

		    		cariPegawai();
		    	});
		    	
		    	$('#btn-prev').click(function(){
		    		var page = $('#page').val();
		    		page--;
		    		$('#page').val(page);

		    		cariPegawai();
		    	});
		    	
		    	$('#btn-simpan-pegawai').click(function(){
		    		var data = {
	    				id: $('#id').val(),
	    				status: $('#status').val(),
	    				kode: $('#kode').val(),
	    				nama: $('#nama').val(),
	    				role: $('#role').val(),
	    				username: $('#username').val(),
	    				password: $('#password').val()
		    		}
		    		
		    		savePegawai(data, function(result){
		    			if (result.message === 'Berhasil!') {
		    				alert('Berhasil Menyimpan Pegawai!');
		    			} else {
		    				alert(result.message);
		    			}
		    			
			    		resetPegawaiDetail();
		    			cariPegawai();
		    		}, errorMessage);
		    	});
		    	
		    	$('#btn-cancel-pegawai').click(function(){
		    		resetPegawaiDetail();
		    	});

		    	$(function() {
				  	$('.selectpicker').selectpicker();
				});

			  	$(function() {
				  	$('.selectpicker').selectpicker({
				  		style: 'btn-info',
				  		size: 4
				  	});
				});
		    });
		  </script>
		  
          <input type="hidden" id="page" />
          <div class="panel panel-default">
			<!-- Default panel contents -->
		    <div class="panel-heading">Data Pegawai</div>
			<div class="panel-body">
          	  <div class="row placeholders">
            	<div class="col-xs-6 col-sm-3 placeholder">
              	  <button class="btn btn-transclute" data-toggle="modal" data-target="#modal-detail-pegawai">
              	    <img src="<c:url value="/resources/images/add.png" />" width="50" />
					<p>
					  <span class="text-muted">Tambah Pegawai</span>
				  	</p>
              	  </button>
            	</div>
              
            	<table class="table"> <!-- Bootstrap's table -->
				  <tr>
			    	<td width="200">
					  <select class="selectpicker" id="searchBy">
					    <option value="" selected>- Cari Berdasarkan : -</option>
					    <option value="kode">Kode</option>
						<option value="nama">Nama</option>
					  </select>
			    	</td>
			    	<td>
		      	  	  <div class="input-group">
		        	    <input type="text" class="form-control" id="query" />
		        		<span class="input-group-btn">
		          	  	  <button class="btn btn-primary" id="btn-cari-pegawai">Cari!</button>
		      			</span>
		          	  </div>
			    	</td>
			  	  </tr>
				</table>

            	<div class="table-responsive">
              	  <table class="table table-striped text-center" id="table-pegawai">
                	<thead>
                  	  <tr>
                    	<th class="center">Kode</th>
                    	<th class="center">Nama</th>
                    	<th class="center">Role</th>
                    	<th class="center">Username</th>
                    	<th class="center">Password</th>
                    	<th>&nbsp;</th>
                  	  </tr>
                	</thead>
                	<tbody>
                	</tbody>
              	  </table>
            	</div>
            	<table class="table text-center">
            	  <tr>
					<td colspan="6">
				  	  <div class="btn-group btn-group-md">
				  	  	<button class="btn btn-primary" id="btn-prev">SEBELUMNYA</button>
				  	  	<button class="btn btn-primary" id="btn-next">SELANJUTNYA</button>
				  	  </div>
				  	</td>
            	  </tr>
            	</table>
              </div><!-- row-placeholder -->
		    </div><!-- panel-body -->
          </div><!-- panel -->
		  <div class="modal fade" id="modal-detail-pegawai" role="dialog">
		    <div class="modal-dialog">
		      <div class="modal-content">
		        <div class="modal-header">
		          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
		          <h4 class="modal-title">Data Pegawai</h4>
		        </div>
		        <div class="modal-body">
		          <form role="form">
	    			<input type="hidden" id="id" value="0" />
	    			<input type="hidden" id="status" value="AKTIF" />
		            <div class="form-group">
		              <label for="kode" class="control-label">Kode Pegawai:</label>
		              <input type="text" class="form-control" id="kode" />
		            </div>
		            <div class="form-group">
		              <label for="nama" class="control-label">Nama Pegawai:</label>
		              <input type="text" class="form-control" id="nama" />
		            </div>
		            <div class="form-group">
		              <label for="role" class="control-label">Role Pegawai:</label>
					  <select class="selectpicker" id="role">
					    <option value="" selected>Pilih Role :</option>
						<option value="OWNER">OWNER</option>
						<option value="OPERATOR">OPERATOR</option>
					  </select>
		            </div>
		            <div class="form-group">
		              <label for="username" class="control-label">Username:</label>
					  <input type="text" class="form-control" id="username" />
		            </div>
		            <div class="form-group">
		              <label for="password" class="control-label">Password:</label>
					  <input type="text" class="form-control" id="password" />
		            </div>
		          </form>
		        </div><!-- modal-body -->
		        <div class="modal-footer">
		          <button class="btn btn-default" id="btn-cancel-pegawai" data-dismiss="modal">Close</button>
				  <button class="btn btn-primary" id="btn-simpan-pegawai" data-dismiss="modal">Simpan</button>
		        </div>
		      </div><!-- /.modal-content -->
		    </div><!-- /.modal-dialog -->
		  </div><!-- /.modal -->

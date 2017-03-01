<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>

<div class="row">

	<div class="col-xs-12">
		<div class="box box-primary">

			<!-- /.box-header -->
			<div class="box-body">

				<div class="row ">

					<div class="col-xs-9">
						<button class="btn btn-sm btn-primary " id="btnNew">
							<span class="glyphicon glyphicon-plus"></span> 添加
						</button>

						<button class="btn btn-sm btn-primary" id="btnRefresh">
							<span class="glyphicon glyphicon-refresh"></span> 刷新
						</button>

					</div>
					<div class="col-xs-3">

						<div class="input-group">
							<input id="searchInput" type="text" placeholder="请输入关键字"
								class="input-sm form-control"> <span
								class="input-group-btn">
								<button id="searchBtn" type="button"
									class="btn btn-sm btn-primary">搜索</button>
							</span>
						</div>

					</div>
				</div>

				<table id="list"
					class="table table-bordered table-striped table-hover">
					<thead>
						<tr>
							<th>ID</th>
							<th>用户名</th>
							<th>用户姓名</th>
							<th>组织机构</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody></tbody>
				</table>
			</div>
			<!-- /.box-body -->
		</div>
		<!-- /.box -->
	</div>
	
	
</div>

<div id="addModal" class="modal fade" tabindex="0"
	data-backdrop="static" role="dialog" aria-labelledby=""
	aria-hidden="true">
	<div class="modal-dialog" style="width: 70%;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">添加</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal" role="form" id="userForm" >
					<div class="form-group sr-only">
						<label class="col-sm-2 control-label" for="id">Id</label>
						<div class="col-sm-4">
							<input class="form-control" id="id" type="text" name="id" />
						</div>
						<label class="col-sm-2 control-label" for="version">Version</label>
						<div class="col-sm-4">
							<input class="form-control" id="version" type="text" name="version"/>
						</div>
					</div>
					<div class="form-group">						
						<label class="col-sm-2 control-label" for="username">用户名</label>
						<div class="col-sm-4">
							<input class="form-control" id="username" name="username" type="text" />
						</div>
						<label class="col-sm-2 control-label" for="name">用户姓名</label>
						<div class="col-sm-4">
							<input class="form-control" id="name" name="name" type="text" />
						</div>						
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="password">密码</label>
						<div class="col-sm-4">
							<input class="form-control" id="password" name="password" type="text" />
						</div>
						<label class="col-sm-2 control-label" for="rpwd">重复密码</label>
						<div class="col-sm-4">
							<input class="form-control" id="rpwd" name="rpwd" type="text"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="orgId">单位</label>
						<div class="col-sm-4">
							<select class="form-control" id="orgId" name="orgId" type="text">
							<option>1</option>
							</select>
						</div>
						<label class="col-sm-2 control-label" for="deptId">部门</label>
						<div class="col-sm-4">
							<input class="form-control" id="deptId" name="deptId"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="roleId">角色</label>
						<div class="col-sm-4">
							<input class="form-control" id="roleId" name="roleId" />
						</div>
						
					</div>

				</form>				
			
			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
				<button class="btn btn-primary" id="btnSave">保存</button>
			</div>
		</div>
	</div>
</div>


<script type="text/javascript"
	src="${pageContext.request.contextPath}/assets/js/dist/sys/userList.js"></script>
# git 使用

# git init

# git add .

# git commit -m "your commit message"

# git remote add origin {your repository git url}

# git pull origin master

# git push origin master

# 存在需要合并的冲突,合并冲突重新提交
# vi files      :w & :q  end this

# 再次 commit & push


# 创建分支
# 创建本地分支
# git branch 新分支名
# 切换本地分支
# git checkout 新分支名
# 上传本地分支到github上
# git push origin 新分支名
# 删除远程分支
# git push origin :develop

# 修改分支名称
git branch -m main master
git fetch origin
git branch -u origin/master master
git remote set-head origin -a
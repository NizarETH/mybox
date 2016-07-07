require 'colorize'
require 'rubygems'
require 'json'
require 'net/http'
require 'open-uri'
require 'fileutils'

class Gitcreate


    def initialize
        #by ETTAHERI Nizar
        # Visibility_level :
        # 20 for Public
        # 10 for Internal
        # 0  for Private
        #namespace_id
        # @Visibility_level=0
        @token="u5Tdy4mUXZm8F28Vj1oj"#"9r124KzqK-_UtE6LRnzB"
        @user="euphordev04"#"UnessMacOS"
        @host="git.paperpad.fr"#"devserver"
        @path="http://#{@host}/api/v3/groups?private_token=#{@token}"

    end

    def getTab
        path = URI(@path)
        puts "path : #{path}"
        content = Net::HTTP.get(path)
        tab = JSON.parse(content)
         nmbr=-1
        tab.each do |name|
            nameGroup=name["name"]
           
            nmbr+=1
            puts" name :#{nameGroup}, rank :#{nmbr}".blue
           
        end
        tab.each do |id|
            id=id["id"]
            puts" id of group :#{id} ".yellow

        end
        @tab = tab
        @tab

    end
    def choose
        puts "Add to group, hit 1 (Yes) else 0 (No), 2 to libs".yellow
        cl=STDIN.gets.to_i
        if cl ==1
            p=1
            end
         if cl == 0
            p=0
            end
         if cl == 2
            p=2
        end
        p
    end

    def getGroup(tab)
        puts "rank of group :".blue
        @cl=STDIN.gets.to_i

        puts "rank : #{@cl}"
        group = tab["#{@cl}".to_i]["name"]
        group.downcase!
        group.gsub!(' ','-')
        puts "id of namespace :".yellow
        @id=STDIN.gets.to_i
        puts  "group=#{group}".blue
        group


    end
    def gitignore
        puts "Enter number of folders and files to ignore : (to skip hit enter)".yellow
        nmbr=STDIN.gets.to_i

        a=Array.new(nmbr)
        puts "Enter folders and files to ignore : (to skip hit enter)".red
        begin
            puts"#{nmbr} :"
            items=STDIN.gets.chomp()
            cmde="git rm --cached #{items}"
            system(cmde)
            open('.gitignore', 'a') { |f|
              f.puts "\n#{items}"
            }
            nmbr-=1
        end while nmbr>0
        #File.open('.gitignore', 'w') {|f| f.write a.join("\n")}

    end

    def name_from_floder

        puts"name processing...".blue
        name1 = File.basename(Dir.getwd)
        name=name1
        name.downcase!
        name.gsub!(' ','-')
        # name.unicode_normalize!(:kd)
        puts "name : #{name}"
        name
    end

    def create_repo_group(name)
        puts "Creating Git repository #{name}...".blue

        cmd="curl -H \"Content-Type:application/json\" http://#{@host}/api/v3/projects?private_token=#{@token} -d '{\"name\":\"#{name}\",\"visibility_level\": 20 , \"namespace_id\":#{@id}}'"

        puts cmd

        system(cmd)
        puts " done."
    end
    def create_repo(name)
        puts "Creating Git repository #{name}...".blue
        cmd="curl -H \"Content-Type:application/json\" http://#{@host}/api/v3/projects?private_token=#{@token} -d '{\"name\":\"#{name}\",\"visibility_level\": 20 }'"
        puts cmd
        system(cmd)
        puts " done."
    end
    def push_repo_group(group,name)
        puts "Pushing to Git repository #{name}...".blue
        system("git remote remove origin")
        system("git init")
        cmd5="git add -A"
        system(cmd5)
        puts cmd5
        puts "Enter message of commit #{name}...".blue
        msg=STDIN.gets.chomp()
        cmd3="git commit -m \"#{msg}\""
        system(cmd3)
        cmd4="git remote add origin git@#{@host}:#{group}/#{name}.git"#"git remote add origin http://#{@host}/#{group}/#{name}.git"
        puts cmd4
        system(cmd4)
        system("git push -u origin master")
        puts"Done".blue
    end

    def push_repo(name)
        puts "Pushing to Git repository #{name}...".blue
        system("git remote remove origin")
        system("git init")
        cmd5="git add -A"
        system(cmd5)
        puts cmd5
        puts "Enter message of commit #{name}...".blue
        msg=STDIN.gets.chomp()
        cmd3="git commit -m \"#{msg}\""
        system(cmd3)
        cmd4="git remote add origin git@#{@host}:#{@user}/#{name}.git"#"git remote add origin http://#{@host}/#{@user}/#{name}.git"
        puts cmd4
        system(cmd4)
        system("git push -u origin master")
        puts"Done".blue
    end

        def create_repo_library(name)
           
            puts "Creating Git repository #{name}...".blue
            cmd="curl -H \"Content-Type:application/json\" http://#{@host}/api/v3/projects?private_token=#{@token} -d '{\"name\":\"#{name}\",\"visibility_level\": 20 }'"
            puts cmd
            system(cmd)
            puts " done."
          

             end
        def push_repo_library(group,name_lib)

            puts "Pushing to Git repository #{name_lib}...".blue
            system("git remote remove origin")
            system("git init")
            cmd5="git add -A"
            system(cmd5)
            puts cmd5
            puts "Enter message of commit #{name_lib}...".blue
            msg=STDIN.gets.chomp()
            cmd3="git commit -m \"#{msg}\""
            system(cmd3)
            cmd4="git remote add origin git@#{@host}:#{group}/#{name_lib}.git"#"git remote add origin http://#{@host}/#{group}/#{name_lib}.git"
            puts cmd4
            system(cmd4)
            system("git push -u origin master")
            puts"Done".blue
        end

end



    git=Gitcreate.new

    name=git.name_from_floder
    p=git.choose
    puts "It's #{p}"
    case p
    when 2
    tab=git.getTab
    group=git.getGroup(tab)
    git.create_repo_group(name)
    git.push_repo_library(group, name)
    when 1
    git.gitignore
    tab=git.getTab
    group=git.getGroup(tab)
    git.create_repo_group(name)
    git.push_repo_group(group,name)
    when 0
    git.create_repo(name)
    git.push_repo(name)

    else
    puts "I have no idea what to do with that."
end

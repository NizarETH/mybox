require 'colorize'
require 'rubygems'
require 'mini_magick'
require 'json'
require 'net/http'
require 'open-uri'
require 'fileutils'
require 'fastimage'
require 'fastimage_resize'
require './mybox_script/icon'
require './mybox_script/xml'
require './mybox_script/ApiPublish'
require './mybox_script/cloneweb'
require './mybox_script/parsjson'
require './mybox_script/generatApp'
require './mybox_script/ftp'
require './mybox_script/crash'
require './mybox_script/choosepk12'
require './mybox_script/keystore'




crash=MyBox::Crash.new
pub = MyBox::Publish.new
c = MyBox::Cloneweb.new
icon = MyBox::Iconexport.new
xml = MyBox::XMLBuilder.new
ftp = MyBox::FTPup.new
generat = MyBox::GeneratApp.new
pk = MyBox::Pk12.new
key = MyBox::Keystore.new




puts "Enter 1 to change version code and version name".blue
puts "Enter 0 to skip".blue
cd=STDIN.gets.to_i
case cd

    when 1

    pub.updateVersionCode
    pub.updateVersionName

    when 0

    puts" Skip".green
end

puts "Enter 2 to get other options.".blue
puts "Enter 1 to fast generate.".blue
puts "Enter 0 to normal generate.".blue
nmbre = STDIN.gets.to_i
puts "It's : #{nmbre}"
case nmbre
     when 1
       puts "Enter 3 to upload all the apps.".green
       puts "Enter 2 to generate apps from list.".green
       puts "Enter 1 to generate app.".green
       i = STDIN.gets.to_i
       i
         case i
         when 1
            puts "Enter id of application".blue
            id = STDIN.gets.to_i
            puts " id of application #{id}" .blue
           pj = MyBox::Pars_json.new(id)
           tab = pj.ParsJson

           pub.choose_language(tab,id)
           puts"The play directory.".blue

           #pub.ParsJson
           puts"Parsing of JSON for application's icon.".blue
           pub.EditDir
           puts"GENERATION ETAPE.".blue
           puts"Update of directories.".blue
           pub.EditFile
           puts"Update of files.".blue
           puts"Update of Build.gradle".blue
           pub.EditBuild
           puts"Update of web Home.".red
           c.name_compare(tab, false)
           puts"Update of pk12 file".green
           pk.choose_pk(id)
           puts"done.".red
           puts"Generate icons.".blue

           icon.image_from_json(tab)
           icon.export
           puts"Generate XML.".blue


           var1, var2, var3, var4 = xml.ParsJson(tab,id)

           puts "Name : #{var1}"
           puts "App id: #{var2}"
           puts "Sender_id: #{var3}"
           puts "Google Maps Key: #{var4}"
           xml.BuildXml_from_JSON(var1, var2, var3, var4)
           xml.BuildXml_Map_from_JSON(var4)


           numline = key.read_array(id)
           key.key_update_droid(numline)

           generat.generatAPK
           #generat.install_app
           #generat.publishListing
           generat.publishApp
           #var1= ftp.updateVersionName
           #puts "version :#{var1}"

           #ftp.update_version(tab,id)

           #ftp.updat_name_app(tab,id)
           #ftp.update_name_pack(tab,id)
           #var = var1[0].to_s
           #ftp.version_name(var)

           #ftp.uplaod_ftp(tab,id)


        when 2
          path_to_file="./app/src/main/play"
          FileUtils.remove_dir(path_to_file) if File.exist?(path_to_file)
          file="./input.txt"
          File.open(file, "r") do |f|
              f.each_line do |line|
                  puts line
                  id=line.gsub(/\D/, "")

              puts " id of application #{id}" .blue

                  pj=MyBox::Pars_json.new(id)
                  tab=pj.ParsJson

                  pub.choose_language(tab,id)
                  puts"The play directory.".blue

                  #pub.ParsJson
                  puts"Parsing of JSON for application's icon.".blue
                  pub.EditDir
                  puts"GENERATION ETAPE.".blue
                  puts"Update of directories.".blue
                  pub.EditFile
                  puts"Update of files.".blue
                  puts"Update of Build.gradle".blue
                  pub.EditBuild
                  puts"Update of web Home.".red
                  c.name_compare(tab, false)
                  puts"Update of pk12 file".green
                  pk.choose_pk(id)
                  puts"done.".red
                  puts"Generate icons.".blue

                  icon.image_from_json(tab)
                  icon.export
                  puts"Generate XML.".blue


                  var1, var2, var3, var4, var5 = xml.ParsJson(tab,id)

                  puts "Name : #{var1}"
                  puts "App id: #{var2}"
                  puts "Sender_id: #{var3}"
                  puts "console_id : #{var4}"
                  puts "bandeau : #{var5}"

                  xml.BuildXml_from_JSON(var1, var2, var3, var4, var5)
                  xml.BuildXml_Map_from_JSON(var4)

                  numline = key.read_array(id)
                  key.key_update_droid(numline)
                  generat.generatAPK
                  #generat.install_app
                  #generat.publishListing
                  generat.publishApp
                  #var1= ftp.updateVersionName
                  #puts "version :#{var1}"

                  #ftp.update_version(tab,id)

                  #ftp.updat_name_app(tab,id)
                  #ftp.update_name_pack(tab,id)
                  #var=var1[0].to_s
                  #ftp.version_name(var)

                  #ftp.uplaod_ftp(tab,id)

                end
              end


    when 3
      path_to_file="./app/src/main/play"
     FileUtils.remove_dir(path_to_file) if File.exist?(path_to_file)


      @myTab = ["./Paperpad Business.txt", "./Paperpad Business 2.txt", "./Paperpad.txt", "./Custom.txt"]
      @myTab.each { |a|
          puts" tab : #{a}".blue

         text = File.read(a)
         File.readlines(a).each do |line|
           puts line
           id=line.gsub(/\D/, "")

           puts " id of application #{id}" .blue

           pj=MyBox::Pars_json.new(id)
           tab=pj.ParsJson

           pub.choose_language(tab,id)
           puts"The play directory.".blue


           puts"Parsing of JSON for application's icon.".blue
           pub.EditDir
           puts"GENERATION ETAPE.".blue
           puts"Update of directories.".blue
           pub.EditFile
           puts"Update of files.".blue
           puts"Update of Build.gradle".blue
           pub.EditBuild
           puts"Update of web Home.".red
           c.name_compare(tab, true)

           puts"Update of pk12 file".green
           pk.choose_pk(id)
           puts"Generate icons.".blue

           icon.image_from_json(tab)
           icon.export
           puts"Generate XML.".blue


           var1, var2, var3, var4, var5 = xml.ParsJson(tab,id)

           puts "Name : #{var1}"
           puts "App id: #{var2}"
           puts "Sender_id: #{var3}"
           puts "console_id : #{var4}"
           puts "bandeau : #{var5}"

           xml.BuildXml_from_JSON(var1, var2, var3, var4, var5)
           xml.BuildXml_Map_from_JSON(var4)
           numline = key.read_array(id)
           key.key_update_droid(numline)
           generat.generatAPK
           #generat.install_app
           generat.publishApp

          end
          }
        end


    when 0

    path_to_file="./app/src/main/play"
    FileUtils.remove_dir(path_to_file) if File.exist?(path_to_file)
    puts "Enter id of application".blue
    id=STDIN.gets.to_i
    pj=MyBox::Pars_json.new(id)
    tab=pj.ParsJson

    pub.choose_language(tab,id)
    puts"The play directory.".blue

    #pub.ParsJson(tab)
    puts"Parsing of JSON for application's icon.".blue
    pub.EditDir
    puts"GENERATION ETAPE.".blue
    puts"Update of directories.".blue
    pub.EditFile
    puts"Update of files.".blue
    puts"Update of Build.gradle".blue
    pub.EditBuild
    puts"Update of pk12 file".green
    pk.choose_pk(id)
    c.name_compare(tab, false)
    puts"done.".red




    puts"Generate icons.".blue

    icon.image_from_json(tab)
    icon.export
    puts"Generate XML.".blue

    var1, var2, var3, var4, var5 = xml.ParsJson(tab,id)

    puts "Name : #{var1}"
    puts "App id: #{var2}"
    puts "Sender_id: #{var3}"
    puts "console_id : #{var4}"
    puts "bandeau : #{var5}"

    xml.BuildXml_from_JSON(var1, var2, var3, var4, var5)
    xml.BuildXml_Map_from_JSON(var4)

    p=generat.UpdateChooseGeneration


    puts "It's : #{p}"
    case p

        when 1
          numline = key.read_array(id)
          key.key_update_droid(numline)
        generat.generatAPK

        when 0
        puts "skip."
    end
    loop do
        g=generat.UpdateChoose1
        puts "It's : #{g}"
        case g
            when 1
            generat.publishApp
            when 2
            generat.publishListing
            when 3
            var1= ftp.updateVersionName
            puts "var1 :#{var1}"

            ftp.update_version(tab,id)

            ftp.updat_name_app(tab,id)
            ftp.update_name_pack(tab,id)
            var=var1[0].to_s
            ftp.version_name(var)

            ftp.upload_ftp(tab,id)
            when 4
            numline = key.read_array(id)
            key.key_update_droid(numline)
            generat.install_app
            when 5
            crash.crash_up
            #when 6
            #crash.crash_mybox
            when 0
            puts "skip.".red
            break

            break if g==0

        end
    end
    when 2
    loop do
            g=generat.UpdateChoose1
            puts "It's : #{g}"
            case g
                when 1
                generat.publishApp
                when 2
                generat.publishListing
                when 3
                var1= ftp.updateVersionName
                puts "La version :#{var1}"

                ftp.update_version(tab,id)

                ftp.updat_name_app(tab,id)
                ftp.update_name_pack(tab,id)
                var=var1[0].to_s
                ftp.version_name(var)

                ftp.upload_ftp(tab,id)
                when 4
                numline = key.read_array(id)
                key.key_update_droid(numline)
                generat.install_app
                when 5
                crash.crash_up

                when 0
                puts "skip.".red
                break

                break if g==0

            end
        end


end
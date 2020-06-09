package ftn.sbnz.banhammer.service.implementation;

import ftn.sbnz.banhammer.model.DRL;
import ftn.sbnz.banhammer.repository.DRLRepository;
import ftn.sbnz.banhammer.service.DRLService;
import org.apache.maven.shared.invoker.*;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class DRLServiceImpl implements DRLService {

    @Autowired
    DRLRepository drlRepository;

    @Autowired
    KieContainer kieContainer;

    private void cleanInstall () throws RuntimeException, MavenInvocationException {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile( new File( "..\\drools-spring-kjar\\pom.xml" ) );
        ArrayList<String> goals = new ArrayList<String>();
        goals.add("clean");
        goals.add("install");
        request.setGoals(goals);
        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(System.getenv("M2_HOME")));
        invoker.execute( request );
    }

    public DRL create(DRL drl, String text) {
        String path = "..\\drools-spring-kjar\\src\\main\\resources\\sbnz\\integracija\\" + drl.getName() + ".drl";
        try {
            if(new File(path).exists()){
                return null;
            }
            PrintWriter out = new PrintWriter(new File(path));
            out.println(text);
            drl.setPath(path);
            out.close();
            cleanInstall();
            return this.save(drl);
        } catch (MavenInvocationException | FileNotFoundException e){
            return null;
        } catch (RuntimeException e){
            new File(path).delete();
            return null;
        }
    }

    public String getContent(DRL drl) {
        try{
            return  new String(Files.readAllBytes(Paths.get(drl.getPath())));
        }catch (Exception e){
            return "";
        }
    }

    public void update(Long id, String text){
        DRL drl = this.findOne(id);
        String oldContent = this.getContent(drl);
        File f = new File(drl.getPath());
        if(f.exists()){
            try{
                PrintWriter out = new PrintWriter(f);
                out.println(text);
                out.close();
                cleanInstall();
            }catch (Exception e){
                try {
                    PrintWriter out = new PrintWriter(f);
                    out.println(oldContent);
                    out.close();
                    cleanInstall();
                }catch (Exception ignored){ }
            }

        }
    }

    public void delete(Long id){
        DRL drl = this.findOne(id);
        String oldContent = this.getContent(drl);
        File f = new File(drl.getPath());
        if(f.exists()){
            try{
                f.delete();
                cleanInstall();
            }catch (Exception e){
                try {
                    PrintWriter out = new PrintWriter(f);
                    out.println(oldContent);
                    out.close();
                    cleanInstall();
                }catch (Exception ignored){ }
            }
        }

        drlRepository.delete(drl);
    }

    public List<DRL> findAll() {
        return drlRepository.findAll();
    }

    public DRL save(DRL drl) {
        return drlRepository.save(drl);
    }

    public DRL findOne(Long id) {return drlRepository.findById(id).orElse(null);}
}

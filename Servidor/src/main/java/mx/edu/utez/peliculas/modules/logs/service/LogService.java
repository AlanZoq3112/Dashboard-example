package mx.edu.utez.peliculas.modules.logs.service;

import mx.edu.utez.peliculas.modules.logs.model.Log;
import mx.edu.utez.peliculas.modules.logs.model.LogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {
    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void registrarLog(Log log) {
        logRepository.save(log);
    }

    public List<Log> getAll(){
        return logRepository.findAll();
    }
}

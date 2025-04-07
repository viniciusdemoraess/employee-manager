package com.seplag.employee_manager.domain.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.seplag.employee_manager.application.io.ServidorEfetivoPerUnitResponse;
import com.seplag.employee_manager.application.io.ServidorEfetivoRequest;
import com.seplag.employee_manager.application.io.ServidorEfetivoResponse;
import com.seplag.employee_manager.domain.entity.Cidade;
import com.seplag.employee_manager.domain.entity.Endereco;
import com.seplag.employee_manager.domain.entity.FotoPessoa;
import com.seplag.employee_manager.domain.entity.Lotacao;
import com.seplag.employee_manager.domain.entity.PessoaEndereco;
import com.seplag.employee_manager.domain.entity.ServidorEfetivo;
import com.seplag.employee_manager.domain.entity.Unidade;
import com.seplag.employee_manager.infrastructure.pesistence.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static java.util.Objects.nonNull;

@Slf4j
@Service
@AllArgsConstructor
public class ServidorEfetivoService {

    private final ServidorEfetivoRepository repository;
    private final EnderecoRepository enderecoRepository;
    private final LotacaoRepository lotacaoRepository;
    private final CidadeRepository cidadeRepository;
    private final PessoaEnderecoRepository pessoaEnderecoRepository;
    private final UnidadeRepository unidadeRepository;
    private final MinioService minioService;
    private final FotoPessoaRepository fotoPessoaRepository;
    
    @Transactional
    public ServidorEfetivoResponse create(ServidorEfetivoRequest request) {
        Cidade cidadeSalva = salvarCidade(request);
        Endereco enderecoSalvo = salvarEndereco(request, cidadeSalva);
        ServidorEfetivo servidorSalvo = salvarServidor(request);

        associarEndereco(servidorSalvo, enderecoSalvo);
        Unidade unidade = associarLotacao(servidorSalvo, request);
        List<String> urlsFotos = salvarFotos(servidorSalvo, request.fotos());

        Lotacao lotacaoDoServidor = unidade.getLotacoes()
            .stream()
            .filter(l -> l.getPessoa().equals(servidorSalvo))
            .findFirst()
            .orElse(null);

        return new ServidorEfetivoResponse(
            servidorSalvo.getId(),
            servidorSalvo.getNome(), 
            servidorSalvo.getDataNascimento(), 
            servidorSalvo.getSexo(),
            servidorSalvo.getMae(), 
            servidorSalvo.getPai(), 
            enderecoSalvo.getTipoLogradouro(),
            enderecoSalvo.getLogradouro(),
            enderecoSalvo.getNumero(), 
            enderecoSalvo.getBairro(),
            cidadeSalva.getNome(), 
            cidadeSalva.getUf(), 
            unidade.getNome(), 
            servidorSalvo.getMatricula(),
            lotacaoDoServidor.getDataLotacao(), 
            lotacaoDoServidor.getPortaria(),
            urlsFotos
        );
        
    }

    @Transactional
    public ServidorEfetivoResponse update(Long id, ServidorEfetivoRequest request) {
        ServidorEfetivo servidor = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Servidor não encontrado com id: " + id));

        if (request.nome() != null) servidor.setNome(request.nome());
        if (request.dataNascimento() != null) servidor.setDataNascimento(request.dataNascimento());
        if (request.sexo() != null) servidor.setSexo(request.sexo());
        if (request.mae() != null) servidor.setMae(request.mae());
        if (request.pai() != null) servidor.setPai(request.pai());
        if (request.matricula() != null) servidor.setMatricula(request.matricula());

        Cidade cidadeSalva = salvarCidade(request);
        Endereco enderecoSalvo = atualizarEndereco(servidor, request, cidadeSalva);
        associarEndereco(servidor, enderecoSalvo);

        Unidade unidade = associarLotacao(servidor, request);

        List<String> urlsFotos = salvarFotos(servidor, request.fotos());

        Lotacao lotacaoDoServidor = unidade.getLotacoes().stream()
            .filter(l -> l.getPessoa().equals(servidor))
            .findFirst()
            .orElse(null);

        return new ServidorEfetivoResponse(
            servidor.getId(),
            servidor.getNome(),
            servidor.getDataNascimento(),
            servidor.getSexo(),
            servidor.getMae(),
            servidor.getPai(),
            enderecoSalvo.getTipoLogradouro(),
            enderecoSalvo.getLogradouro(),
            enderecoSalvo.getNumero(),
            enderecoSalvo.getBairro(),
            cidadeSalva.getNome(),
            cidadeSalva.getUf(),
            unidade.getNome(),
            servidor.getMatricula(),
            lotacaoDoServidor != null ? lotacaoDoServidor.getDataLotacao() : null,
            lotacaoDoServidor != null ? lotacaoDoServidor.getPortaria() : null,
            urlsFotos
        );
    }

    private Cidade salvarCidade(ServidorEfetivoRequest request) {
        return cidadeRepository.findByNomeIgnoreCaseAndUfIgnoreCase(
                    request.cidade(), request.uf()
                )
                .orElseGet(() -> {
                    Cidade nova = new Cidade();
                    nova.setNome(request.cidade());
                    nova.setUf(request.uf());
                    return cidadeRepository.save(nova);
                });
    }

    private Endereco salvarEndereco(ServidorEfetivoRequest request, Cidade cidade) {
        return enderecoRepository.findByLogradouroIgnoreCaseAndNumeroAndBairroIgnoreCaseAndCidade(
            request.logradouro(), request.numero(), request.bairro(), cidade
        )
        .orElseGet(() -> {
            Endereco novo = new Endereco();
            novo.setTipoLogradouro(request.tipoLogradouro());
            novo.setLogradouro(request.logradouro());
            novo.setNumero(request.numero());
            novo.setBairro(request.bairro());
            novo.setCidade(cidade);
            return enderecoRepository.save(novo);
        });
    }

    private Endereco atualizarEndereco(ServidorEfetivo servidor, ServidorEfetivoRequest request, Cidade cidade) {
        Endereco endereco = servidor.getPessoaEnderecos().stream()
            .findFirst()
            .map(PessoaEndereco::getEndereco)
            .orElseGet(Endereco::new); // novo se não existir nenhum vinculado
    
        endereco.setTipoLogradouro(request.tipoLogradouro());
        endereco.setLogradouro(request.logradouro());
        endereco.setNumero(request.numero());
        endereco.setBairro(request.bairro());
        endereco.setCidade(cidade);
    
        return enderecoRepository.save(endereco);
    }
    

    private ServidorEfetivo salvarServidor(ServidorEfetivoRequest request) {
        ServidorEfetivo servidor = new ServidorEfetivo();
        servidor.setNome(request.nome());
        servidor.setDataNascimento(request.dataNascimento());
        servidor.setSexo(request.sexo());
        servidor.setMae(request.mae());
        servidor.setPai(request.pai());
        servidor.setMatricula(request.matricula());
        return repository.save(servidor);
    }

    private void associarEndereco(ServidorEfetivo servidor, Endereco endereco) {
        PessoaEndereco pessoaEndereco = new PessoaEndereco(null, servidor, endereco);
        pessoaEnderecoRepository.save(pessoaEndereco);

        servidor.getPessoaEnderecos().clear();
        servidor.getPessoaEnderecos().addAll(List.of(pessoaEndereco));
    }

    private Unidade associarLotacao(ServidorEfetivo servidor, ServidorEfetivoRequest request) {
        Unidade unidadeSalva = unidadeRepository.findById(request.unidadeId())
            .orElseThrow(() -> new EntityNotFoundException("Unidade nao encontrada!"));
        Lotacao lotacao = new Lotacao(null, servidor, unidadeSalva, request.dataLotacao(), null, request.portaria());
        unidadeSalva.getLotacoes().add(lotacaoRepository.save(lotacao));
        return unidadeRepository.save(unidadeSalva);
    }

    private List<String> salvarFotos(ServidorEfetivo servidor, List<MultipartFile> fotos) {
        List<String> urlsFotos = new ArrayList<>();

        if (fotos != null && !fotos.isEmpty()) {
            List<FotoPessoa> fotoPessoas = fotos.stream().map(foto -> {
                try {
                    String fileName = gerarNomeComHash(foto);
                    minioService.uploadFile(foto, fileName);

                    String urlTemporaria = minioService.getFileUrl(fileName);
                     urlsFotos.add(urlTemporaria);
                    return new FotoPessoa(null, servidor, LocalDate.now(), "servidores", fileName);
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao salvar foto", e);
                }
            }).collect(Collectors.toList());
            fotoPessoaRepository.saveAll(fotoPessoas);
        }
        return urlsFotos;
    }

    private String gerarNomeComHash(MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid + extension;
    }


    public ServidorEfetivoResponse getById(Long id) {
        ServidorEfetivo servidor = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Servidor Efetivo não encontrado, ID: " + id));
        return mapToResponse(servidor);      
    }

    @Transactional
    public void delete(Long id) {
        ServidorEfetivo servidor = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Servidor não encontrado com id: " + id));

        List<FotoPessoa> fotos = fotoPessoaRepository.findByPessoa(servidor);
        fotos.forEach(foto -> {
            try {
                minioService.deleteFile(foto.getHash());
            } catch (Exception e) {
                log.warn("Erro ao deletar arquivo do MinIO: {}", foto.getHash(), e);
            }
        });
        fotoPessoaRepository.deleteAll(fotos);

        List<Lotacao> lotacoes = lotacaoRepository.findByPessoa(servidor);
        lotacaoRepository.deleteAll(lotacoes);

        pessoaEnderecoRepository.deleteAll(servidor.getPessoaEnderecos());

        repository.delete(servidor);
    }

    public Page<ServidorEfetivoResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable)
            .map(this::mapToResponse);
    }

    public InputStream getImage(String fileName){
        try {
                String urlTemporaria = minioService.getFileUrl(fileName);

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(urlTemporaria))
                        .GET()
                        .build();

                HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

                if (response.statusCode() == 200) {
                    return response.body();
                } else {
                    throw new RuntimeException("Falha ao obter imagem. Código HTTP: " + response.statusCode());
                }
            } catch (Exception e) {
                throw new RuntimeException("Erro ao buscar imagem", e);
            }    
    }

    private ServidorEfetivoResponse mapToResponse(ServidorEfetivo servidor) {
        PessoaEndereco pessoaEndereco = servidor.getPessoaEnderecos()
            .stream()
            .findFirst()
            .orElse(null);
    
        Endereco endereco = pessoaEndereco != null ? pessoaEndereco.getEndereco() : null;
        Cidade cidade = endereco != null ? endereco.getCidade() : null;
    
        Lotacao lotacao = lotacaoRepository.findByPessoa(servidor)
            .stream()
            .findFirst()
            .orElse(null);
    
        Unidade unidade = lotacao != null ? lotacao.getUnidade() : null;
    
        List<String> urlsFotos = fotoPessoaRepository.findByPessoa(servidor)
            .stream()
            .map(f -> {
                try {
                    return minioService.getFileUrl(f.getHash());
                } catch (Exception e) {
                    return null;
                }
            })
            .filter(f -> f != null)
            .collect(Collectors.toList());
    
        return new ServidorEfetivoResponse(
            servidor.getId(),
            servidor.getNome(),
            servidor.getDataNascimento(),
            servidor.getSexo(),
            servidor.getMae(),
            servidor.getPai(),
            endereco != null ? endereco.getTipoLogradouro() : null,
            endereco != null ? endereco.getLogradouro() : null,
            endereco != null ? endereco.getNumero() : null,
            endereco != null ? endereco.getBairro() : null,
            cidade != null ? cidade.getNome() : null,
            cidade != null ? cidade.getUf() : null,
            unidade != null ? unidade.getNome() : null,
            servidor.getMatricula(),
            lotacao != null ? lotacao.getDataLotacao() : null,
            lotacao != null ? lotacao.getPortaria() : null,
            urlsFotos
        );
    }

    public ServidorEfetivoPerUnitResponse mapToServidorUnidade(ServidorEfetivo servidor) {
        Lotacao lotacao = lotacaoRepository.findByPessoa(servidor)
            .stream()
            .findFirst()
            .orElse(null);

        Optional<FotoPessoa> fotoOptional = fotoPessoaRepository.findByPessoa(servidor)
            .stream()
            .findFirst();
        
        String urlFoto = fotoOptional
            .map(f -> minioService.getFileUrl(f.getHash()))
            .orElse(null);
    
        Unidade unidade = lotacao != null ? lotacao.getUnidade() : null;

        int idade = Period.between(servidor.getDataNascimento(), LocalDate.now()).getYears(); 

        return new ServidorEfetivoPerUnitResponse(servidor.getNome(), idade, 
            nonNull(unidade) ? unidade.getNome() : null, urlFoto);

    }

    public Page<ServidorEfetivoPerUnitResponse> listarPorUnidade(Long unidId, Pageable pageable) {
        return lotacaoRepository.findServidoresEfetivosPorUnidade(unidId, pageable)
            .map(this::mapToServidorUnidade);
    }
    

    
}

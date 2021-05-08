package ru.nsu.kokorina.distributedsystems.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.nsu.kokorina.distributedsystems.OSMProcessor;
import ru.nsu.kokorina.distributedsystems.model.NodeDTO;
import ru.nsu.kokorina.distributedsystems.model.NodeEntity;
import ru.nsu.kokorina.distributedsystems.model.Tag;
import ru.nsu.kokorina.distributedsystems.repository.NodeRepository;
import ru.nsu.kokorina.distributedsystems.repository.TagRepository;

import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/node")
public class NodeController {

    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private TagRepository tagRepository;

    private OSMProcessor osmProcessor;

    public NodeController(OSMProcessor osmProcessor) {
        this.osmProcessor = osmProcessor;
    }

    @GetMapping("/{id}")
    public NodeDTO getNode(@PathVariable("id") Long id) {
        NodeEntity nodeEntity = nodeRepository.findById(id).orElseThrow(NullPointerException::new);
        return nodeEntity.convertToDTO();
    }

    @PostMapping
    public NodeDTO createNode(@RequestBody NodeDTO nodeDTO) {
        List<Tag> tags = nodeDTO.getTags().entrySet().stream()
                .map(t -> Tag.builder()
                        .key(t.getKey())
                        .value(t.getValue())
                        .build()).collect(Collectors.toList());
        NodeEntity nodeEntity = NodeEntity.builder()
                .username(nodeDTO.getUsername())
                .lon(nodeDTO.getLon())
                .lat(nodeDTO.getLat())
                .tags(tags)
                .build();
        tagRepository.saveAll(tags);
        nodeRepository.save(nodeEntity);
        return nodeEntity.convertToDTO();
    }

    @PutMapping("/{id}")
    public NodeDTO updateNode(@PathVariable("id") Long id, @RequestBody NodeDTO nodeDTO) {
        NodeEntity nodeEntity = NodeEntity.builder()
                .username(nodeDTO.getUsername())
                .lon(nodeDTO.getLon())
                .lat(nodeDTO.getLat())
                .tags(nodeDTO.getTags().entrySet().stream()
                        .map(t -> Tag.builder()
                                .key(t.getKey())
                                .value(t.getValue())
                                .build()).collect(Collectors.toList()))
                .build();
        NodeEntity node = nodeRepository.findById(id).orElseThrow(NullPointerException::new);
        nodeEntity.setId(node.getId());
        nodeRepository.save(nodeEntity);
        return nodeEntity.convertToDTO();
    }

    @DeleteMapping("/{id}")
    public void deleteNode(@PathVariable("id") long id) {
        nodeRepository.deleteById(id);
    }

    @GetMapping("/getNodesInArea")
    public List<NodeDTO> getNodes(@RequestParam("lat") Double lat,
                                  @RequestParam("lon") Double lon,
                                  @RequestParam("rad") Double rad) {
        List<NodeDTO> nodeDTOS = new ArrayList<>();
        List<NodeEntity> nodes = nodeRepository.getNodesInArea(lat, lon, rad);
        nodes.forEach(n -> {
            NodeDTO nodeDTO = n.convertToDTO();
            nodeDTOS.add(nodeDTO);
        });
        return nodeDTOS;
    }

    @PostMapping("/init")
    public void init(@RequestParam("path") String path) {
        try (InputStream inputStream = new FileInputStream(path)) {
            osmProcessor.process(inputStream);
        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
        }
    }

    private NodeEntity dtoToEntity(NodeDTO nodeDTO) {
        List<Tag> tags = nodeDTO.getTags().entrySet().stream()
                .map(t -> Tag.builder()
                        .key(t.getKey())
                        .value(t.getValue())
                        .build()).collect(Collectors.toList());
        return NodeEntity.builder()
                .username(nodeDTO.getUsername())
                .lon(nodeDTO.getLon())
                .lat(nodeDTO.getLat())
                .tags(tags)
                .build();
    }
}

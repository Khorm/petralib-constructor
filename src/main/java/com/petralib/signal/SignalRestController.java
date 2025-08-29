package com.petralib.signal;

//@RestController
//@RequestMapping("/api/v1/signal")
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//@RequiredArgsConstructor
@Deprecated
public class SignalRestController {

//    BlockMapper blockMapper;
//    BlockService blockService;
//
//    @ProjectGrant(userAction = UserAction.WRITE)
//    @PostMapping
//    public ResponseEntity<?> save(@RequestParam Long projectId, @RequestParam String blockType,
//                                  @Valid @RequestBody SignalVariablesDto signalVariablesDto, Errors errors) {
//        if (errors.hasErrors()) {
//            Collection<String> validationErrors = errors.getAllErrors().stream()
//                    .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
//            return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
//        }
//        BlockEntity blockEntity = blockMapper.fromDtoToEntity(signalVariablesDto);
//
//        return ResponseEntity.ok("ok");
//    }
}

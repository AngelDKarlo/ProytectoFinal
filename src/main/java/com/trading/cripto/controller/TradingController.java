package com.trading.cripto.controller;

import com.trading.cripto.dto.TradeRequest;
import com.trading.cripto.dto.TradeResponse;
import com.trading.cripto.model.Transaction;
import com.trading.cripto.model.Wallet;
import com.trading.cripto.service.TradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trading")
@CrossOrigin(origins = "*")
public class TradingController {

    @Autowired
    private TradingService tradingService;

    /**
     * Ejecutar una operación de trading
     * POST /api/trading/execute
     */
    @PostMapping("/execute")
    public ResponseEntity<TradeResponse> ejecutarTrade(
            HttpServletRequest request,
            @Valid @RequestBody TradeRequest tradeRequest) {

        // Obtener userId del token JWT
        Integer userId = (Integer) request.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).body(
                    new TradeResponse(false, "Usuario no autenticado"));
        }

        TradeResponse response = tradingService.ejecutarTrade(userId, tradeRequest);

        if (response.isExitoso()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Obtener wallets del usuario autenticado
     * GET /api/trading/wallets
     */
    @GetMapping("/wallets")
    public ResponseEntity<?> obtenerWallets(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).body(
                    Map.of("error", "Usuario no autenticado"));
        }

        List<Wallet> wallets = tradingService.obtenerWalletsUsuario(userId);
        return ResponseEntity.ok(wallets);
    }

    /**
     * Obtener historial de transacciones del usuario autenticado
     * GET /api/trading/history
     */
    @GetMapping("/history")
    public ResponseEntity<?> obtenerHistorial(
            HttpServletRequest request,
            @RequestParam(defaultValue = "50") int limit) {

        Integer userId = (Integer) request.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).body(
                    Map.of("error", "Usuario no autenticado"));
        }

        List<Transaction> transacciones = tradingService.obtenerHistorialTransacciones(userId);

        // Limitar resultados
        if (limit > 0 && limit < transacciones.size()) {
            transacciones = transacciones.subList(0, limit);
        }

        return ResponseEntity.ok(Map.of(
                "total", transacciones.size(),
                "transactions", transacciones
        ));
    }

    /**
     * Obtener detalle de una wallet específica
     * GET /api/trading/wallet/{cryptoSymbol}
     */
    @GetMapping("/wallet/{cryptoSymbol}")
    public ResponseEntity<?> obtenerWalletDetalle(
            HttpServletRequest request,
            @PathVariable String cryptoSymbol) {

        Integer userId = (Integer) request.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).body(
                    Map.of("error", "Usuario no autenticado"));
        }

        // Implementación pendiente en TradingService
        return ResponseEntity.ok(Map.of(
                "message", "Endpoint en construcción",
                "symbol", cryptoSymbol
        ));
    }
}